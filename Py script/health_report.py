import os
import shutil
import subprocess
from datetime import datetime

# ---------- CONFIG ----------
CRITICAL_SERVICES = ['nginx', 'docker']  # Add more services as needed
DISK_USAGE_THRESHOLD = 80  # percent
REPORT_FILE = 'complete_devops_health_report.txt'
# ----------------------------

def get_logged_in_users():
    try:
        users = subprocess.check_output("who", shell=True, text=True)
        return f"[Logged-in Users]\n{users.strip() or 'No users logged in.'}"
    except Exception as e:
        return f"[Logged-in Users] ERROR: {e}"

def get_memory_usage():
    try:
        meminfo = {}
        with open('/proc/meminfo') as f:
            for line in f:
                key, value = line.split(':')
                meminfo[key.strip()] = int(value.strip().split()[0])
        
        total = meminfo['MemTotal']
        free = meminfo['MemFree'] + meminfo.get('Buffers', 0) + meminfo.get('Cached', 0)
        used = total - free
        swap_total = meminfo['SwapTotal']
        swap_free = meminfo['SwapFree']
        swap_used = swap_total - swap_free

        mem_usage = f"[Memory Usage] RAM: {used / 1024:.2f} MB / {total / 1024:.2f} MB used"
        swap_usage = f"[Swap Usage] SWAP: {swap_used / 1024:.2f} MB / {swap_total / 1024:.2f} MB used"
        return mem_usage + "\n" + swap_usage
    except Exception as e:
        return f"[Memory/Swap Usage] ERROR: {e}"

def get_cpu_usage():
    try:
        load1, load5, load15 = os.getloadavg()
        return f"[Load Average] 1min: {load1:.2f}, 5min: {load5:.2f}, 15min: {load15:.2f}"
    except Exception as e:
        return f"[Load Average] ERROR: {e}"

def get_uptime():
    try:
        with open('/proc/uptime') as f:
            uptime_seconds = float(f.readline().split()[0])
            days = int(uptime_seconds // 86400)
            hours = int((uptime_seconds % 86400) // 3600)
            minutes = int((uptime_seconds % 3600) // 60)
            return f"[Uptime] {days}d {hours}h {minutes}m"
    except Exception as e:
        return f"[Uptime] ERROR: {e}"

def get_disk_usage(path="/"):
    try:
        total, used, free = shutil.disk_usage(path)
        percent_used = used / total * 100
        status = f"OK"
        if percent_used > DISK_USAGE_THRESHOLD:
            status = f"WARNING: Over {DISK_USAGE_THRESHOLD}%"
        return f"[Disk Usage] {path} - {used // (2**30)} GB / {total // (2**30)} GB used ({percent_used:.2f}%) - {status}"
    except Exception as e:
        return f"[Disk Usage] ERROR: {e}"

def check_service_status(service_name):
    try:
        result = subprocess.run(["systemctl", "is-active", service_name], capture_output=True, text=True)
        status = result.stdout.strip()
        if status != "active":
            return f"[Service Check] {service_name}: NOT RUNNING"
        return f"[Service Check] {service_name}: running"
    except Exception as e:
        return f"[Service Check] {service_name}: ERROR - {e}"

def generate_full_report():
    lines = []
    lines.append(f"📄 DevOps Full Health Report - {datetime.now()}\n")

    # System Info Checks
    lines.append(get_logged_in_users())
    lines.append(get_memory_usage())
    lines.append(get_cpu_usage())
    lines.append(get_uptime())
    lines.append(get_disk_usage("/"))

    # Service Checks
    lines.append("\n[Service Status Checks]")
    for service in CRITICAL_SERVICES:
        lines.append(check_service_status(service))

    # Save to file
    with open(REPORT_FILE, 'w') as f:
        for line in lines:
            f.write(line + "\n\n")

    print(f"✅ Full system report saved to: {REPORT_FILE}")

if __name__ == "__main__":
    generate_full_report()
