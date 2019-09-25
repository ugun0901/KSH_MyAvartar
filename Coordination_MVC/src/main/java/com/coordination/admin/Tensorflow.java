package com.coordination.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tensorflow {
	
	private static final Logger logger = LoggerFactory.getLogger(Tensorflow.class);
	
	//상의 이미지 분석
	public void Upper_Tensorflow(String image)
	{
		String s = null;
		try {
			
			System.out.println("Executing Python Code");
			
			String upperPath = "C:\\Python\\Lib\\site-packages\\"
					+ "tensorflow\\examples\\label_image\\upper.py";
			
			String[] upper = new String[3];
			upper[0] = "Python";
			upper[1] = upperPath;
			upper[2] = image;
			//추론 이미지 경로와 동일해야함
			
			Runtime.getRuntime().exec(upper);
			System.out.println("----상의 이미지 자르기 완료----");
			System.out.println();
			System.out.println("----이미지 추론 시작----");
			
			//이미지 추론
			String PythonScriptPath = "C:\\Python\\Lib\\site-packages\\tensorflow"
					+ "\\examples\\label_image\\admin_upper.py";
			String[] cmd = new String[7];
			cmd[0] = "Python";
			cmd[1] = PythonScriptPath;
			cmd[2] = "--input_layer=Mul";
			cmd[3] = "--output_layer=final_result";
			cmd[4] = "--labels=C:\\tmp\\top\\retrain_labels.txt";
			cmd[5] = "--graph=C:\\tmp\\top\\retrain_graph.pb";
			cmd[6] = "--image=" + image;
			
			Process process = Runtime.getRuntime().exec(cmd);
			
			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(process.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(process.getErrorStream()));
			
			while((s = stdInput.readLine()) != null)
			{
				System.out.println(s);
			}
			
			while((s = stdError.readLine()) != null)
			{
				System.out.println(s);
			}
			
		}catch(Exception e){
			logger.info("**********Error!! (Upper_Tensorflow())**********");
			System.out.println("**********Error!! (Upper_Tensorflow())**********");
			e.printStackTrace();
			
			System.exit(-1);
		}
	}
	
	//하의 이미지 분석
	public void Lower_Tensorflow(String image)
	{
		System.out.println();
		
		//이미지명 → 이미지명-1.jpg
		image = image.substring(0, image.length()-4);
		image += "-1.jpg";
		String s = null;
		try {
			
			System.out.println("Executing Python Code");
			
			String lowerPath = "C:\\Python\\Lib\\site-packages\\"
					+ "tensorflow\\examples\\label_image\\lower.py";
			
			String[] lower = new String[3];
			lower[0] = "Python";
			lower[1] = lowerPath;
			lower[2] = image;
			//추론 이미지 경로와 동일해야함
			
			Runtime.getRuntime().exec(lower);
			System.out.println("----하의 이미지 자르기 완료----");
			System.out.println();
			System.out.println("----이미지 추론 시작----");
			
			//이미지 추론
			String PythonScriptPath = "C:\\Python\\Lib\\site-packages\\tensorflow"
					+ "\\examples\\label_image\\admin_lower.py";
			String[] cmd = new String[7];
			cmd[0] = "Python";
			cmd[1] = PythonScriptPath;
			cmd[2] = "--input_layer=Mul";
			cmd[3] = "--output_layer=final_result";
			cmd[4] = "--labels=C:\\tmp\\top\\retrain_labels.txt";
			cmd[5] = "--graph=C:\\tmp\\top\\retrain_graph.pb";
			cmd[6] = "--image=" + image;
			
			Process process = Runtime.getRuntime().exec(cmd);
			
			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(process.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(process.getErrorStream()));
			
			while((s = stdInput.readLine()) != null)
			{
				System.out.println(s);
			}
			
			while((s = stdError.readLine()) != null)
			{
				System.out.println(s);
			}
			
		}catch(Exception e){
			logger.info("**********Error!! (Upper_Tensorflow())**********");
			System.out.println("**********Error!! (Upper_Tensorflow())**********");
			e.printStackTrace();
			
			System.exit(-1);
		}
	}
	
	//이미지 복구
	public void restore(String image)
	{

		try {
			
			String restore = image.substring(0, image.length()-4);
			restore += "-2.jpg";
			System.out.println(restore);
			
			String[] cmdd = new String[3];
			cmdd[0] = "Python";
			cmdd[1] = "C:\\Python\\Lib\\site-packages\\tensorflow\\"
					+ "examples\\label_image\\restore.py";
			cmdd[2] = restore;
			
			Process restore_process = Runtime.getRuntime().exec(cmdd);
			
			System.out.println("----이미지 복구 완료----");
		}catch(Exception e) {
			logger.info("**********Error!! (restore())**********");
			System.out.println("**********Error!! (restore())**********");
			e.printStackTrace();
			
			System.exit(-1);
		}
	}
}