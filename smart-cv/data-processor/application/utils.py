from pathlib import Path

def get_file_extensions(filename):
    return Path(filename).suffix[1:]