package freemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		try {
			cfg.setClassForTemplateLoading(Test.class, "/"); // 指定模板所在的classpath目录
			cfg.setSharedVariable("upperFC", new UpperFirstCharacter()); // 添加一个"宏"共享变量用来将属性名首字母大写
			Template t = cfg.getTemplate("javabean.temp"); // 指定模板
			FileOutputStream fos = new FileOutputStream(new File(
					"c:/Student.java")); // java文件的生成目录

			// 模拟数据源
			Map data = new HashMap();
			data.put("package", "edu"); // 包名
			data.put("className", "Student");

			List pros = new ArrayList();
			Map pro_1 = new HashMap();
			pro_1.put("proType", String.class.getSimpleName());
			pro_1.put("proName", "name");
			pros.add(pro_1);

			Map pro_2 = new HashMap();
			pro_2.put("proType", String.class.getSimpleName());
			pro_2.put("proName", "sex");
			pros.add(pro_2);

			Map pro_3 = new HashMap();
			pro_3.put("proType", Integer.class.getSimpleName());
			pro_3.put("proName", "age");
			pros.add(pro_3);

			data.put("properties", pros);
			t.process(data, new OutputStreamWriter(fos, "utf-8")); //
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

}