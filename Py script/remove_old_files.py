import os
import time

BACKUP_DIR = "/backup/"
AGE_DAYS = 5

def remove_old_files(path, days_old):
    now = time.time()
    cutoff = now - (days_old * 86400)

    for filename in os.listdir(path):
        filepath = os.path.join(path, filename)
        if os.path.isfile(filepath) and os.path.getmtime(filepath) < cutoff:
            os.remove(filepath)
            print(f"Deleted: {filepath}")

if __name__ == "__main__":
    remove_old_files(BACKUP_DIR, AGE_DAYS)
