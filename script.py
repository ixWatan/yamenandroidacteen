import os

def find_java_files(directory):
    java_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".java"):
                java_files.append(os.path.join(root, file))
    return java_files

def extract_class_code(file_path):
    class_code = []
    try:
        with open(file_path, "r") as f:
            lines = f.readlines()
            class_started = False
            for line in lines:
                # Detect the start of the class definition
                if not class_started and line.strip().startswith("public class"):
                    class_started = True
                # Append lines after the class declaration starts
                if class_started:
                    class_code.append(line)
            # Remove unnecessary empty lines
            class_code = [line for line in class_code if line.strip()]
    except Exception as e:
        print(f"Error reading file {file_path}: {e}")
    return class_code

def format_output(filename, class_code):
    file_basename = os.path.basename(filename)
    formatted_output = f"<b><font size='5'>{file_basename}:</font></b><br><font size='3'>"
    formatted_output += "<br>".join(class_code)
    formatted_output += "</font><br><br>"
    return formatted_output

def main():
    project_directory = "."  # The current directory where the script is located
    java_files = find_java_files(project_directory)
    
    if not java_files:
        print("No Java files found.")
        return

    html_output = ""
    for file in java_files:
        class_code = extract_class_code(file)
        if class_code:
            html_output += format_output(file, class_code)
        else:
            print(f"No class found in {file}")

    try:
        # Write the formatted output to an HTML file
        with open("java_classes.html", "w") as f:
            f.write("<html><body>")
            f.write(html_output)
            f.write("</body></html>")
        print("HTML file generated: java_classes.html")
    except Exception as e:
        print(f"Error writing HTML file: {e}")

if __name__ == "__main__":
    main()
