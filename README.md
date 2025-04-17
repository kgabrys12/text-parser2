# Text Processing Application

This Java application processes text files, extracting sentences and producing both XML and CSV outputs.

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher

## Building the JAR

To build the application:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Usage

Execute the application using the following command pattern:

```bash
java -jar text-parser2-1.0-SNAPSHOT-jar-with-dependencies.jar <input-file>
```

Where `<input-file>` is the path to the text file you want to process.

## Output Files

The application generates two output files in the same directory as the input file:

1. **XML Output**: `<input-filename>.xml`
    - Contains structured representation of sentences and words
    - Each sentence is enclosed in `<sentence>` tags
    - Each word is enclosed in `<word>` tags

2. **CSV Output**: `<input-filename>.csv`
    - Contains tabular representation of sentences
    - Each row represents a sentence with comma-separated words

## Example

If you process a file named `sample.txt`:

```bash
java -jar text-parser2-1.0-SNAPSHOT-jar-with-dependencies.jar sample.txt
```

The application will generate:
- `sample.xml` - XML format output
- `sample.csv` - CSV format output

Both files will be saved in the same directory as the original input file.