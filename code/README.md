## Run the project

### 1. Build the project (downloads dependencies + compiles)
mvn clean install

### 2. Run the application
mvn exec:java

### Notes
- Make sure `sample.pnml` and `sample.spec` are present in the project root (or update the paths in the code).
- Ensure NuSMV is installed and the path is correctly configured.