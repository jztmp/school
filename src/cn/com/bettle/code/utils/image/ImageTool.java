package cn.com.bettle.code.utils.image;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @see 处理图片的工具类
 * @version 1.0
 * */
public class ImageTool implements Serializable {

	public static void main(String[] args) {
		bean.bringCode(null, null);// 生成随机的注册验证码
		
		bean.cut("D:\\opt\\", "/log/http_imgload2.jpg", 200, 100, 100, 100);// 裁剪图片
		
		bean.reduce("D:\\opt\\", "/log/http_imgload2.jpg", 500, 500, true);// 压缩图片
		
		bean.water("D:\\opt\\log\\http_imgload2.jpg", bean.outputFile);// 绘制水印
		
		bean.rotate("D:\\opt\\", "/log/http_imgload2.jpg", 90, null);// 旋转图片
		
		bean.zoom("D:\\opt\\", "/log/http_imgload2.jpg", 2, null);// 放大或缩小图片
		
		bean.fontImg("D:\\opt\\log\\http_imgload2.jpg", "中国", new Font("Atlantic Inline", Font.PLAIN, 18), Color.GREEN, 200, 100);// 把字符印到图片上
	}
	
	private static final long serialVersionUID = 1L;
	private String outputFile = "D:\\test\\aa.jpg";
	
	
	/**
	 * @see 生成随机的注册验证码图像
	 * @param request
	 * @param response
	 * */
	public void bringCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			int width = 65, height = 24;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = image.getGraphics();// 获取图形上下文
			graphics.setColor(getRandColor(180, 250));// 设定背景色(最多色调255)
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(Color.black);// 画边框
			graphics.drawRect(0, 0, width - 1, height - 1);
			String randCode = "";// 取随机产生的认证码
			for (int i = 0; i < 4; ++i) {// 4代表4位验证码,如果要生成更多位的认证码,则加大数值
				randCode += mapTable[(int) (mapTable.length * Math.random())];
			}
			graphics.setColor(Color.black);// 将认证码显示到图像中,如果要生成更多位的认证码,增加drawString语句
			graphics.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
			String str = randCode.substring(0, 1);
			graphics.drawString(str, 8, 17);
			str = randCode.substring(1, 2);
			graphics.drawString(str, 20, 15);
			str = randCode.substring(2, 3);
			graphics.drawString(str, 35, 18);
			str = randCode.substring(3, 4);
			graphics.drawString(str, 45, 15);
			Random rand = new Random();// 随机产生30个干扰点
			for (int i = 0; i < 30; i++) {
				int x = rand.nextInt(width);
				int y = rand.nextInt(height);
				graphics.drawOval(x, y, 1, 1);
			}
			graphics.dispose();// 释放图形上下文
			
			/**如果是做网页验证码，请使用这两行代码*/
			//request.getSession().setAttribute("checkCode", randCode);// 认证码保存在session中
			//ImageIO.write(image, "JPEG", response.getOutputStream());// 输出图像到页面
			
			/**如果是生成文件，请使用这四行代码*/
			OutputStream output = new FileOutputStream(outputFile);
			ImageIO.write(image, "JPEG", output);
			output.flush();
			output.close();
			
			image.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see 裁剪图片
	 * @param base 根目录
	 * @param imgPath 原图片路径(虚拟路径)
	 * @param left 左边的起点
	 * @param top 顶部的起点
	 * @param width 要裁剪的宽度
	 * @param height 要裁剪的高度
	 * @return String
	 * */
	@SuppressWarnings("unchecked")
	public String cut(String base, String imgPath, int left, int top, int width, int height) {
		try {
			String suffix = imgPath.substring(imgPath.lastIndexOf(".") + 1);//后缀名
			Iterator readers = ImageIO.getImageReadersByFormatName(suffix);// 取得图片读入器
			ImageReader reader = (ImageReader) readers.next();
			InputStream source = new FileInputStream(base + imgPath);// 取得图片读入流
			ImageInputStream input = ImageIO.createImageInputStream(source);
			reader.setInput(input, true);
			ImageReadParam param = reader.getDefaultReadParam();// 图片参数
			Rectangle rect = new Rectangle(left, top, width, height);//开始裁剪
			param.setSourceRegion(rect);
			BufferedImage image = reader.read(0, param);
			String newFile = getNewPath(imgPath);
			ImageIO.write(image, suffix, new File(base + newFile));
			image.flush();
			input.flush();
			input.close();
			source.close();
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @see 压缩图片
	 * @param base 根目录
	 * @param imgPath 要压缩的图片路径(虚拟路径)
	 * @param width 压缩宽
	 * @param height 压缩高
	 * @param percentage 是否等比例压缩,true则宽高比自动调整
	 * @return String
	 */
	public String reduce(String base, String imgPath, int width, int height, boolean percentage) {
		try {
			File srcfile = new File(base + imgPath);
			BufferedImage src = ImageIO.read(srcfile);
			if (percentage) {
				double rate1 = ((double) src.getWidth(null)) / (double) width + 0.1;
				double rate2 = ((double) src.getHeight(null)) / (double) height + 0.1;
				double rate = rate1 > rate2 ? rate1 : rate2;
				width = (int) (((double) src.getWidth(null)) / rate);
				height = (int) (((double) src.getHeight(null)) / rate);
			}
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);				
			image.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
			String newFile = getNewPath(imgPath);
			FileOutputStream out = new FileOutputStream(base + newFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			image.flush();
			out.flush();
			out.close();
			src.flush();
			return newFile;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @see 绘制水印
	 * @param imgPath 图片路径(物理路径)
	 * @param waterPath 水印图片路径(物理路径)
	 * */
	public void water(String imgPath, String waterPath) {
		try {
			String extend = imgPath.substring(imgPath.lastIndexOf("."));
			if (!".gif".toLowerCase().equals(extend)) {// 如果图片不是gif类型就加水印
				BufferedImage src = ImageIO.read(new File(imgPath));
	            int width = src.getWidth();
	            int height = src.getHeight();
	            BufferedImage water = ImageIO.read(new File(waterPath));
	            int waWidth = water.getWidth();
	            int waHeight = water.getHeight();
	            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            Graphics graph = image.createGraphics();
	            graph.drawImage(src, 0, 0, width, height, null);
	            if(height > waHeight * 2 && width > waWidth * 2) {
	            	graph.drawImage(water, 0, height-waHeight, waWidth, waHeight, null);
		            graph.dispose();
		            OutputStream out = new FileOutputStream(imgPath);
		            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		            encoder.encode(image);
		            out.flush();
		            out.close();
	            }
	            image.flush();
	            water.flush();
	            src.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see 旋转图片
	 * @param base 根目录
	 * @param imgPath 图片路径(虚拟路径)
	 * @param angle 旋转角度(90为顺时针旋转,-90为逆时针旋转)
	 * @param response 可以为null
	 * @return
	 */
	public String rotate(String base, String imgPath, int angle, HttpServletResponse response) {
		try {
			if (angle % 90 == 0) {
				File file = new File(base + imgPath);
				BufferedImage src = ImageIO.read(file);
				int width = src.getWidth();
				int height = src.getHeight();
				BufferedImage image = new BufferedImage(height, width, src.getColorModel().getTransparency());
				Graphics2D graphics2d = image.createGraphics();
				if (angle < 0){
					graphics2d.rotate(Math.toRadians(angle), width / 2, width / 2);
				}else{
					graphics2d.rotate(Math.toRadians(angle), height / 2, height / 2);
				}
				graphics2d.drawImage(src, 0, 0, null);
				graphics2d.dispose();
				String suffix = imgPath.substring(imgPath.lastIndexOf(".") + 1).toUpperCase();
				String newFile = getNewPath(imgPath);
				ImageIO.write(image, suffix, new File(base + newFile));
				if (null != response) {
					ImageIO.write(image, suffix, response.getOutputStream());
				}
				image.flush();
				src.flush();
				return newFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @see 放大或缩小图片
	 * @param base 根目录
	 * @param imgPath 图片路径(虚拟路径)
	 * @param ratio 放大或缩小的倍数(1倍不会改变原图)
	 * @param response 可以为null
	 * @return
	 */
	public String zoom(String base, String imgPath, double ratio, HttpServletResponse response) {
		try {
			BufferedImage src = ImageIO.read(new File(base + imgPath));
	        int width = new Double(src.getWidth() * ratio).intValue();
	        int height = new Double(src.getHeight() * ratio).intValue();
	        AreaAveragingScaleFilter filter = new AreaAveragingScaleFilter(width, height);
	        FilteredImageSource source = new FilteredImageSource(src.getSource(), filter);
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	        Graphics graphics = image.createGraphics();
	        graphics.drawImage(new Canvas().createImage(source), 0, 0, null);
	        String suffix = imgPath.substring(imgPath.lastIndexOf(".") + 1).toUpperCase();
	        String newFile = getNewPath(imgPath);
	        ImageIO.write(image, suffix, new File(base + newFile));
	        if (null != response) {
				ImageIO.write(image, suffix, response.getOutputStream());
			}
	        image.flush();
	        src.flush();
	        return newFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @see 把字符印到图片上
	 * @param imgPath 图片路径(物理路径)
	 * @param text 准备的字符
	 * @param font 字体样式
	 * @param color 字体颜色
	 * @param left 图片的x坐标 (印的位置)
	 * @param top 图片的y坐标 (印的位置)
	 * @return
	 */
	public void fontImg(String imgPath, String text, Font font, Color color, int left, int top) {
		try {
			BufferedImage src = ImageIO.read(new File(imgPath));
            int width = src.getWidth();
            int height = src.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graph = image.createGraphics();
            graph.drawImage(src, 0, 0, width, height, null);
            graph.setColor(color);
            graph.setFont(font);
            graph.drawString(text, left, top);
            graph.dispose();
            FileOutputStream out = new FileOutputStream(imgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            image.flush();
            src.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 给定范围获得一个随机颜色
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	// 根据原图片路径获得即将产生的路径
	private String getNewPath(String imgPath) {
		String filePrex = imgPath.substring(0, imgPath.lastIndexOf('.'));
		return filePrex + "0" + imgPath.substring(filePrex.length());
	}
	
	private char mapTable[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static ImageTool bean = new ImageTool();
	
	/**
	 * @see 以单例模式创建，获得对象实例
	 * @return Image
	 */
	public static ImageTool getBean() {
		return bean; 
	}
	
}
