/**
 * Copyright (c) 2009 Simon Denier
 * Released under the MIT License (see LICENSE file)
 */
package valmo.geco.model.iocsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import valmo.geco.core.Util;


/**
 * @author Simon Denier
 * @since Jan 3, 2009
 * 
 */
public class CsvWriter {

	private BufferedWriter writer;

	private String filepath;

	private String csvSep;

	
	public CsvWriter() {
		this(",");
	}

	public CsvWriter(String csvSep) {
		this.csvSep = csvSep;
	}
	
	public CsvWriter(String csvSep, String filePath) throws IOException {
		this(csvSep);
		initialize(filePath);
		open();
	}

	
	public CsvWriter initialize(String filePath) {
		this.filepath = filePath;
		return this;
	}
	
	public CsvWriter initialize(String baseDir, String filename) {
		this.filepath = baseDir + File.separator + filename;
		return this;
	}

	public String filePath() {
		return filepath;
	}

	public String getCsvSep() {
		return csvSep;
	}

	public void setCsvSep(String csvSep) {
		this.csvSep = csvSep;
	}

	public void open() throws IOException {
		this.writer = new BufferedWriter(new FileWriter(filePath()));
	}

	public void close() throws IOException {
		this.writer.close();
	}

	public void write(String str) throws IOException {
		this.writer.write(str);
	}
	
	public void writeRecord(String[] record, String csvSep) throws IOException {
		write(Util.join(record, csvSep, new StringBuffer()));
		this.writer.newLine();
	}
	
	public void writeRecord(String... record) throws IOException {
		writeRecord(record, this.csvSep);
	}

}
