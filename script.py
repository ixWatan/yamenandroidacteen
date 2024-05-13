import os

def find_fragment_files(directory):
    fragment_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".java") or file.endswith(".kt"):
                fragment_files.append(os.path.join(root, file))
    return fragment_files

def extract_fragment_names(file_path):
    fragment_names = []
    with open(file_path, "r") as f:
        lines = f.readlines()
        for line in lines:
            if "class " in line and "extends Fragment" in line:
                class_name = line.split("class ")[1].split(" ")[0]
                fragment_names.append(class_name+ ":")
    return fragment_names

def main():
    project_directory = "D:\Coding\yamenandroidacteen"  # Replace with your project directory
    fragment_files = find_fragment_files(project_directory)

    fragment_names = []
    for file in fragment_files:
        names = extract_fragment_names(file)
        fragment_names.extend(names)

    for name in fragment_names:
        print(name)

if __name__ == "__main__":
    main()
