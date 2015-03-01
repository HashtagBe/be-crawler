package com.hashtag.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hashtag.domain.Article;

public class ExcelExporter {
	private static final int initialSize = 100000;
	public static final String url = "Url";
	public static final String title = "Title";
	public static final String subTitle = "SubTitle";
	public static final String author = "Authors";
	public static final String date = "Date";
	public static final String images = "Images";
	public static final String paragraphs = "Paragraphs";

	public static byte[] generateExcelStream(List<Article> articles)
			throws IOException {
		Workbook wb = getWorkbook(articles);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(initialSize);
		wb.write(baos);
		return baos.toByteArray();
	}

	private static Workbook getWorkbook(List<Article> articles) {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("articles");
		sheet.setDefaultColumnWidth(20);
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setWrapText(true);

		// 创建第一行
		Row row0 = sheet.createRow(0);

		Cell cell = row0.createCell(0);
		cell.setCellValue(url);
		cell.setCellStyle(style);

		cell = row0.createCell(1);
		cell.setCellValue(title);
		cell.setCellStyle(style);

		cell = row0.createCell(2);
		cell.setCellValue(subTitle);
		cell.setCellStyle(style);

		cell = row0.createCell(3);
		cell.setCellValue(author);
		cell.setCellStyle(style);

		cell = row0.createCell(4);
		cell.setCellValue(date);
		cell.setCellStyle(style);

		cell = row0.createCell(5);
		cell.setCellValue(images);
		cell.setCellStyle(style);

		cell = row0.createCell(6);
		cell.setCellValue(paragraphs);
		cell.setCellStyle(style);
		// 循环添加article信息
		CellStyle style1 = wb.createCellStyle();
		// style1.setWrapText(true);

		int rowIdx = 1;
		for (Article article : articles) {
			Row row = sheet.createRow(rowIdx);

			cell = row.createCell(0);
			cell.setCellValue(article.getUrl());
			cell.setCellStyle(style1);

			cell = row.createCell(1);
			cell.setCellValue(article.getTitle());
			cell.setCellStyle(style1);

			cell = row.createCell(2);
			cell.setCellValue(article.getSubTitle());
			cell.setCellStyle(style1);

			cell = row.createCell(3);
			cell.setCellValue(article.getAuthors());
			cell.setCellStyle(style1);

			cell = row.createCell(4);
			if (article.getDate() != null)
				cell.setCellValue(article.getDate().toGMTString());
			else
				cell.setCellValue(article.getDate());
			cell.setCellStyle(style1);

			cell = row.createCell(5);
			String imgs = article.getImageUrls();
			if (imgs != null && imgs.endsWith(","))
				imgs = imgs.substring(0, imgs.length() - 1);
			cell.setCellValue(imgs);
			cell.setCellStyle(style1);

			cell = row.createCell(6);
			String p = article.getParagraphs();
			if (p == null || p.trim().length() == 0)
				continue;
			cell.setCellValue(article.getParagraphs());
			cell.setCellStyle(style1);

			rowIdx++;
		}
		return wb;
	}
}
