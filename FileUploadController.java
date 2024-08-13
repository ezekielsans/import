    public void convertToCsv(File targetFile) {

        System.out.println("targetFile getName(): " + targetFile.getName());
        System.out.println("targetFile toString(): " + targetFile.toString());

        Path csvDirectory = Paths.get("/home/mis/Public/import_test_2024/csv");
      
     

        // Set the CSV file path
        Path csvFilePath = csvDirectory.resolve("converted_file.csv");

        // Ensure the CSV directory exists
        if (!Files.exists(csvDirectory)) {
            try {
                Files.createDirectories(csvDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //get targetfile
        try (FileInputStream fis = new FileInputStream(targetFile);
                //create workbook
                Workbook workbook = new XSSFWorkbook(fis);
                //open csv-create csv
                FileWriter csvWriter = new FileWriter(csvFilePath.toFile())) {

            //start at index 0
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            StringBuilder csvContent = new StringBuilder();

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    String cellValue = getStringCellValue(cell);

                    if (cellValue.isEmpty()) {
                        cellValue = "-";
                    }

                    csvContent.append(cellValue).append("|");

                }
                System.out.println("CSV CONTENT LENGTH:  " + csvContent.length());
                if (csvContent.length() > 0) {
                    csvContent.setLength(csvContent.length() - 1); // Remove the last comma
                }
                csvContent.append("\n");
            }

            csvWriter.write(csvContent.toString());

            Set<PosixFilePermission> csvPerms = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(csvFilePath, csvPerms);

            System.out.println("Conversion complete. CSV file saved at: " + csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
