package poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import freemarker.Test;
import freemarker.UpperFirstCharacter;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ExportDocImpl {
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		try {
			
			cfg.setClassForTemplateLoading(Test.class, "/"); // 指定模板所在的classpath目录
			cfg.setSharedVariable("upperFC", new UpperFirstCharacter()); // 添加一个"宏"共享变量用来将属性名首字母大写
			Template t = cfg.getTemplate("javabean.temp"); // 指定模板
			FileOutputStream fos = new FileOutputStream(new File(
					"c:/Student.java")); // java文件的生成目录
			
			FileInputStream in = new FileInputStream("c:\\test.doc");// 载入文档
			POIFSFileSystem pfs = new POIFSFileSystem(in);
			HWPFDocument hwpf = new HWPFDocument(pfs);
			Range range = hwpf.getRange();// 得到文档的读取范围
			TableIterator it = new TableIterator(range);
			// 迭代文档中的表格
			while (it.hasNext()) {
				Table tb = (Table) it.next();

				// 模拟数据源
				Map data = new HashMap();
				data.put("package", "edu"); // 包名
				data.put("className", "Student");
				
				List<Map> pros = new ArrayList<>();
				// 迭代行，默认从0开始
				for (int i = 0; i < tb.numRows(); i++) {
					TableRow tr = tb.getRow(i);
					Map pro = new HashMap();
					// 迭代列，默认从0开始
					pro.put("proType", getCellValue(tr, 2));
					pro.put("proComment", getCellValue(tr, 1));
					pro.put("proName", getCellValue(tr, 0));
					pros.add(pro);

				} // end for
				
				data.put("properties", pros);
				t.process(data, new OutputStreamWriter(fos, "utf-8")); //
				fos.flush();
				
			} // end while
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end method

	private static String getCellValue(TableRow tr, int columnIndex) {
		TableCell td = tr.getCell(columnIndex);// 取得单元格
		// 取得单元格的内容
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < td.numParagraphs(); k++) {
			Paragraph para = td.getParagraph(k);
			String s = para.text();
			sb.append(s.replaceAll("\\W$", ""));
		} // end for

		return sb.toString();
	}

}