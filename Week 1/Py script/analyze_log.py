LOG_FILE = "error_log.log"

def count_occurrences(logfile, level):
    with open(logfile, 'r') as f:
        return sum(1 for line in f if level in line)

def list_errors(logfile):
    with open(logfile, 'r') as f:
        return [line.strip() for line in f if 'ERROR' in line]

def main():
    print(f"Analyzing log file: {LOG_FILE}\n")

    print("Count of ERRORs:", count_occurrences(LOG_FILE, 'ERROR'))
    print("Count of WARNINGs:", count_occurrences(LOG_FILE, 'WARNING'))
    print("Count of INFOs:", count_occurrences(LOG_FILE, 'INFO'))

    print("\nList of ERROR messages:")
    for line in list_errors(LOG_FILE):
        print(line)

if __name__ == "__main__":
    main()
