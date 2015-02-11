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
			
			cfg.setClassForTemplateLoading(Test.class, "/"); // ָ��ģ�����ڵ�classpathĿ¼
			cfg.setSharedVariable("upperFC", new UpperFirstCharacter()); // ���һ��"��"�����������������������ĸ��д
			Template t = cfg.getTemplate("javabean.temp"); // ָ��ģ��
			FileOutputStream fos = new FileOutputStream(new File(
					"c:/Student.java")); // java�ļ�������Ŀ¼
			
			FileInputStream in = new FileInputStream("c:\\test.doc");// �����ĵ�
			POIFSFileSystem pfs = new POIFSFileSystem(in);
			HWPFDocument hwpf = new HWPFDocument(pfs);
			Range range = hwpf.getRange();// �õ��ĵ��Ķ�ȡ��Χ
			TableIterator it = new TableIterator(range);
			// �����ĵ��еı��
			while (it.hasNext()) {
				Table tb = (Table) it.next();

				// ģ������Դ
				Map data = new HashMap();
				data.put("package", "edu"); // ����
				data.put("className", "Student");
				
				List<Map> pros = new ArrayList<>();
				// �����У�Ĭ�ϴ�0��ʼ
				for (int i = 0; i < tb.numRows(); i++) {
					TableRow tr = tb.getRow(i);
					Map pro = new HashMap();
					// �����У�Ĭ�ϴ�0��ʼ
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
		TableCell td = tr.getCell(columnIndex);// ȡ�õ�Ԫ��
		// ȡ�õ�Ԫ�������
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < td.numParagraphs(); k++) {
			Paragraph para = td.getParagraph(k);
			String s = para.text();
			sb.append(s.replaceAll("\\W$", ""));
		} // end for

		return sb.toString();
	}

}