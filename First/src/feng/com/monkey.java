package feng.com;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.datatransfer.DataFlavor;  
import java.awt.datatransfer.Transferable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.interfaces.RSAKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apkinfo.api.util.AXmlResourceParser;
import org.apkinfo.api.util.TypedValue;
import org.apkinfo.api.util.XmlPullParser;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import org.apkinfo.api.util.AXmlResourceParser;
import org.apkinfo.api.util.TypedValue;
import org.apkinfo.api.util.XmlPullParser;
import org.omg.CORBA.Environment;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.ibm.icu.impl.locale.AsciiUtil.CaseInsensitiveKey;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.JProgressBar;
/**
 * 
 * @author liaoweifeng
 *
 */
public class monkey {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	private JTextField textField_17;
	private JTextField textField_18;
	private JTextField textField_19;
	private JTextField textField_23;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					monkey window = new monkey();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Map<String,Object> readAPK(String apkUrl){
		ZipFile zipFile;
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			zipFile = new ZipFile(apkUrl);
			Enumeration<?> enumeration = zipFile.entries();
			ZipEntry zipEntry = null;
			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				if (zipEntry.isDirectory()) {

				} else {
					if ("androidmanifest.xml".equals(zipEntry.getName().toLowerCase())) {
						AXmlResourceParser parser = new AXmlResourceParser();
						parser.open(zipFile.getInputStream(zipEntry));
						while (true) {
							int type = parser.next();
							if (type == XmlPullParser.END_DOCUMENT) {
								break;
							}
							String name = parser.getName();
							if(null != name && name.toLowerCase().equals("manifest")){
								for (int i = 0; i != parser.getAttributeCount(); i++) {
									if ("versionName".equals(parser.getAttributeName(i))) {
										String versionName = getAttributeValue(parser, i);
										if(null == versionName){
											versionName = "";
										}
										map.put("versionName", versionName);
									} else if ("package".equals(parser.getAttributeName(i))) {
										String packageName = getAttributeValue(parser, i);
										if(null == packageName){
											packageName = "";
										}
										map.put("package", packageName);
									} else if("versionCode".equals(parser.getAttributeName(i))){
										String versionCode = getAttributeValue(parser, i);
										if(null == versionCode){
											versionCode = "";
										}
										map.put("versionCode", versionCode);
									}
								}
								break;
							}
						}
					}
					
				}
			}
			zipFile.close();
		} catch (Exception e) {
			map.put("code", "fail");
			map.put("error","读取apk失败");
		}
		return map;
	}
	
	private static String getAttributeValue(AXmlResourceParser parser, int index) {
		int type = parser.getAttributeValueType(index);
		int data = parser.getAttributeValueData(index);
		if (type == TypedValue.TYPE_STRING) {
			return parser.getAttributeValue(index);
		}
		if (type == TypedValue.TYPE_ATTRIBUTE) {
			return String.format("?%s%08X", getPackage(data), data);
		}
		if (type == TypedValue.TYPE_REFERENCE) {
			return String.format("@%s%08X", getPackage(data), data);
		}
		if (type == TypedValue.TYPE_FLOAT) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type == TypedValue.TYPE_INT_HEX) {
			return String.format("0x%08X", data);
		}
		if (type == TypedValue.TYPE_INT_BOOLEAN) {
			return data != 0 ? "true" : "false";
		}
		if (type == TypedValue.TYPE_DIMENSION) {
			return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type == TypedValue.TYPE_FRACTION) {
			return Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
			return String.format("#%08X", data);
		}
		if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>", data, type);
	}

	private static String getPackage(int id) {
		if (id >>> 24 == 1) {
			return "android:";
		}
		return "";
	}

	// ///////////////////////////////// ILLEGAL STUFF, DONT LOOK :)
	public static float complexToFloat(int complex) {
		return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
	}

	private static final float RADIX_MULTS[] = { 0.00390625F, 3.051758E-005F, 1.192093E-007F, 4.656613E-010F };
	private static final String DIMENSION_UNITS[] = { "px", "dip", "sp", "pt", "in", "mm", "", "" };
	private static final String FRACTION_UNITS[] = { "%", "%p", "", "", "", "", "", "" };
	private JTextField textField_20;
	private JTextField textField_21;
	
	/**
	 * ��ȡipa
	 */
	public static Map<String,Object> readIPA(String ipaURL){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			File file = new File(ipaURL);
            InputStream is = new FileInputStream(file);
            ZipInputStream zipIns = new ZipInputStream(is);
            ZipEntry ze;
            InputStream infoIs = null;
            while ((ze = zipIns.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    if (null != name && name.toLowerCase().contains("info.plist")) {
                        ByteArrayOutputStream _copy = new ByteArrayOutputStream();
                        int chunk = 0;
                        byte[] data = new byte[1024];
                        while(-1!=(chunk=zipIns.read(data))){
                            _copy.write(data, 0, chunk);
                        }
                        infoIs = new ByteArrayInputStream(_copy.toByteArray());
                        break;
                    }
                }
            }
           
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(infoIs);

    		NSString parameters = (NSString) rootDict.get("CFBundleIdentifier");
    		map.put("package", parameters.toString());
    		// Ӧ�ð汾��
    		parameters = (NSString) rootDict.objectForKey("CFBundleShortVersionString");
    		map.put("versionName", parameters.toString());
    		//Ӧ�ð汾��
    		parameters = (NSString) rootDict.get("CFBundleVersion");
    		map.put("versionCode", parameters.toString());
    		
            /////////////////////////////////////////////////
			infoIs.close();
	        is.close();
            zipIns.close();
            
        } catch (Exception e) {
        	map.put("code", "fail");
            map.put("error","��ȡipa�ļ�ʧ��");
        }
        return map;
	}
	
	/**
	 * Create the application.
	 */
	public monkey() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ImageIcon ii = new ImageIcon("./images/psb.jpg");
		Image image = ii.getImage();
		frame = new JFrame();
		frame.setTitle("Bee安卓测试工具");
		frame.setIconImage(image);
		frame.setBounds(100, 100, 732, 606);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		String devices=""; 
		
		JButton btnNewButton = new JButton("获取设备信息");
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String st = "cmd.exe /c adb devices";
				String devices1;

				try {
					Process p = Runtime.getRuntime().exec(st);
					InputStream is = p.getInputStream();
					InputStreamReader bi = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(bi);
					String message = br.readLine();
					while(message != null && !"".equals(message)){
						System.out.println(message);
						textField_2.setText(message);
						message = br.readLine();
						devices1 = message;
						System.out.println(devices1);
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(14, 126, 138, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("设备名");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 12));
		lblNewLabel.setBounds(19, 17, 54, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("状态");
		label.setFont(new Font("宋体", Font.PLAIN, 12));
		label.setBounds(201, 17, 54, 15);
		frame.getContentPane().add(label);
		
		textField_2 = new JTextField();
		textField_2.setBounds(12, 33, 280, 87);
		frame.getContentPane().add(textField_2);
		
		JLabel label_2 = new JLabel("安装包路径");
		label_2.setFont(new Font("宋体", Font.PLAIN, 12));
		label_2.setBounds(14, 198, 76, 15);
		frame.getContentPane().add(label_2);
		
		textField = new JTextField();
		textField.setBounds(85, 195, 170, 21);
		textField.setTransferHandler(new TransferHandler()  {  
            private static final long serialVersionUID = 1L;  
            @Override  
            public boolean importData(JComponent comp, Transferable t) {  
                try {  
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);  
  
                    String filepath = o.toString();  
                    if (filepath.startsWith("[")) {  
                        filepath = filepath.substring(1);  
                    }  
                    if (filepath.endsWith("]")) {  
                        filepath = filepath.substring(0, filepath.length() - 1);  
                    }  
                    System.out.println(filepath);  
                    textField.setText(filepath);  
                    return true;  
                }  
                catch (Exception e) {  
                    e.printStackTrace();  
                }  
                return false;  
            }  
            @Override  
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {  
                for (int i = 0; i < flavors.length; i++) {  
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {  
                        return true;  
                    }  
                }  
                return false;  
            }  
        });
		frame.getContentPane().add(textField);
		textField.setColumns(10);		
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(117, 223, 146, 23);
		progressBar.setOrientation(JProgressBar.HORIZONTAL);
	    //progressBar.setSize(200, 100);
	    progressBar.setMinimum(0);
	    progressBar.setMaximum(100);
	    progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);
		frame.getContentPane().add(progressBar);
		
		JButton button = new JButton("点击安装");
		button.setFont(new Font("宋体", Font.PLAIN, 12));
		button.setBounds(14, 223, 93, 23);
		frame.getContentPane().add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String path = textField.getText();
				String install = "cmd.exe /c adb install ";
				String msg;
				try {
					/*Runtime rt = Runtime.getRuntime();
					rt.exec(install+path);
					System.out.println(install+path);*/
					
					Process p1 = Runtime.getRuntime().exec(install+path);
					InputStream is1 = p1.getInputStream();
					InputStreamReader bi1 = new InputStreamReader(is1);
					BufferedReader br = new BufferedReader(bi1);
					String message = br.readLine();
					int i=0;
					while(message != null && !"".equals(message)){
						System.out.println(message);						
						progressBar.setValue(i++);
						//textField_2.setText(message);
						message = br.readLine();
						msg = message;
						//System.out.println(msg);						
					}
					JOptionPane.showMessageDialog(null, "安装成功");
				}catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "安装失败");
					//System.out.println("安装失败");
				}
			}
		}); 
		
		JLabel label_3 = new JLabel("卸载包名");
		label_3.setFont(new Font("宋体", Font.PLAIN, 12));
		label_3.setBounds(14, 259, 76, 15);
		frame.getContentPane().add(label_3);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(85, 256, 170, 21);
		frame.getContentPane().add(textField_1);
		
		JButton button_1 = new JButton("点击卸载");
		button_1.setFont(new Font("宋体", Font.PLAIN, 12));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = textField_1.getText();
				String install = "cmd.exe /c adb uninstall ";
				try {
					Process p2 = Runtime.getRuntime().exec(install+path);
					InputStream is2 = p2.getInputStream();
					InputStreamReader bi2 = new InputStreamReader(is2);
					BufferedReader br2 = new BufferedReader(bi2);
					String message = br2.readLine();
					while(message != null && !"".equals(message)){
						System.out.println(message);						
						textField_2.setText(message);
						message = br2.readLine();
						//devices1 = message;
						//System.out.println(devices1);						
					}
					JOptionPane.showMessageDialog(null, "卸载成功");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "卸载失败");
				}
			}
		});
		button_1.setBounds(14, 284, 93, 23);
		frame.getContentPane().add(button_1);
		
		JLabel lblNewLabel_2 = new JLabel("安卓文件拖拽到此：");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(19, 359, 138, 15);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("    包名：");
		lblNewLabel_3.setFont(new Font("宋体", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(66, 384, 67, 15);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel label_4 = new JLabel("   版本：");
		label_4.setFont(new Font("宋体", Font.PLAIN, 12));
		label_4.setBounds(73, 413, 54, 15);
		frame.getContentPane().add(label_4);
		
		//拖拽文件到该文本框显示文本框路径
		textField_3 = new JTextField();
		textField_3.setBounds(134, 356, 164, 21);
		textField_3.setTransferHandler(new TransferHandler()  {  
            private static final long serialVersionUID = 1L;  
            @Override  
            public boolean importData(JComponent comp, Transferable t) {  
                try {  
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);  
  
                    String filepath = o.toString();  
                    if (filepath.startsWith("[")) {  
                        filepath = filepath.substring(1);  
                    }  
                    if (filepath.endsWith("]")) {  
                        filepath = filepath.substring(0, filepath.length() - 1);  
                    }  
                    System.out.println(filepath);  
                    textField_3.setText(filepath);  
                    return true;  
                }  
                catch (Exception e) {  
                    e.printStackTrace();  
                }  
                return false;  
            }  
            @Override  
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {  
                for (int i = 0; i < flavors.length; i++) {  
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {  
                        return true;  
                    }  
                }  
                return false;  
            }  
        });  
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		
		textField_4 = new JTextField();
		textField_4.setBounds(134, 384, 164, 21);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(134, 410, 164, 21);
		frame.getContentPane().add(textField_5);
		textField_5.setColumns(10);
		
		JButton button_2 = new JButton("获取应用信息");
		button_2.setFont(new Font("宋体", Font.PLAIN, 12));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("======apk=========");
				//String apkUrl = "src/150211100441.apk";
				String apkUrl = textField_3.getText();
				Map<String,Object> mapApk = monkey.readAPK(apkUrl);
				
				
				/*for (String key : mapApk.keySet()) {
					System.out.println(key + ":" + mapApk.get(key));
		
					
					textField_2.setText(key + ":" + mapApk.get(key));
				}*/
				StringBuffer sb = new StringBuffer("");
				for (String key : mapApk.keySet()) {
					System.out.println(key + ":" + mapApk.get(key));
					sb.append(mapApk.get(key)+",");
				}
				String[] split = null;
				if(!"".equals(sb.toString())){
					split = sb.toString().substring(0,sb.length()-1).split(",");
					/*for(int i=0; i<split.length; i++){
						System.out.println(split[i]);
					}*/
					textField_4.setText(split[0]);
					textField_5.setText(split[1]);
					
				}

				
				System.out.println("======ipa==========");
				String ipaUrl = textField_6.getText();
				Map<String,Object> mapIpa = monkey.readIPA(ipaUrl);
				/*for (String key : mapIpa.keySet()) {
					System.out.println(key + ":" + mapIpa.get(key));
				}*/
				
				StringBuffer sb1 = new StringBuffer("");
				for (String key : mapIpa.keySet()) {
					System.out.println(key + ":" + mapIpa.get(key));
					sb1.append(mapIpa.get(key)+",");
				}
				String[] split1 = null;
				if(!"".equals(sb1.toString())){
					split1 = sb1.toString().substring(0,sb1.length()-1).split(",");
					/*for(int i=0; i<split.length; i++){
						System.out.println(split[i]);
					}*/
					textField_7.setText(split1[0]);
					textField_8.setText(split1[1]);
				}
			}
		});
		button_2.setBounds(31, 525, 121, 23);
		frame.getContentPane().add(button_2);
		
		JLabel lblIos = new JLabel("IOS文件拖拽到此：");
		lblIos.setFont(new Font("宋体", Font.PLAIN, 12));
		lblIos.setBounds(26, 438, 121, 15);
		frame.getContentPane().add(lblIos);
		
		JLabel label_5 = new JLabel("包名：");
		label_5.setFont(new Font("宋体", Font.PLAIN, 12));
		label_5.setBounds(94, 472, 54, 15);
		frame.getContentPane().add(label_5);
		
		JLabel label_6 = new JLabel("版本：");
		label_6.setFont(new Font("宋体", Font.PLAIN, 12));
		label_6.setBounds(96, 497, 54, 15);
		frame.getContentPane().add(label_6);
		
		textField_6 = new JTextField();
		textField_6.setBounds(134, 438, 164, 21);
		textField_6.setTransferHandler(new TransferHandler()  {  
            private static final long serialVersionUID = 1L;  
            @Override  
            public boolean importData(JComponent comp, Transferable t) {  
                try {  
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);  
  
                    String filepath = o.toString();  
                    if (filepath.startsWith("[")) {  
                        filepath = filepath.substring(1);  
                    }  
                    if (filepath.endsWith("]")) {  
                        filepath = filepath.substring(0, filepath.length() - 1);  
                    }  
                    System.out.println(filepath);  
                    textField_6.setText(filepath);  
                    return true;  
                }  
                catch (Exception e) {  
                    e.printStackTrace();  
                }  
                return false;  
            }  
            @Override  
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {  
                for (int i = 0; i < flavors.length; i++) {  
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {  
                        return true;  
                    }  
                }  
                return false;  
            }  
        });  
		frame.getContentPane().add(textField_6);
		textField_6.setColumns(10);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(134, 466, 164, 21);
		frame.getContentPane().add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(134, 494, 164, 21);
		frame.getContentPane().add(textField_8);
		
		JButton btnNewButton_2 = new JButton("重置");
		btnNewButton_2.setFont(new Font("宋体", Font.PLAIN, 12));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
				textField_5.setText("");
				textField_6.setText("");
				textField_7.setText("");
				textField_8.setText("");
			}
		});
		btnNewButton_2.setBounds(179, 525, 93, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_1 = new JButton("执行momkey测试");
		btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 12));
		btnNewButton_1.setBounds(340, 468, 138, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JLabel lblp = new JLabel("-p 所测试程序的包名");
		lblp.setFont(new Font("宋体", Font.PLAIN, 12));
		lblp.setBounds(326, 76, 138, 15);
		frame.getContentPane().add(lblp);
		
		textField_9 = new JTextField();
		textField_9.setBounds(516, 73, 184, 21);
		frame.getContentPane().add(textField_9);
		textField_9.setColumns(10);
		
		JLabel label_1 = new JLabel("-throttle 每个行为的延时(ms)");
		label_1.setFont(new Font("宋体", Font.PLAIN, 12));
		label_1.setBounds(326, 101, 170, 15);
		frame.getContentPane().add(label_1);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(516, 98, 67, 21);
		frame.getContentPane().add(textField_10);
		
		JLabel lblv = new JLabel("-v 输出结果详细级别");
		lblv.setFont(new Font("宋体", Font.PLAIN, 12));
		lblv.setBounds(326, 126, 138, 15);
		frame.getContentPane().add(lblv);
		
		String[] level = {"0","1","2"};
		JComboBox comboBox = new JComboBox(level);
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				System.out.println(comboBox.getSelectedItem().toString());
			}
		});
		comboBox.setBounds(517, 123, 46, 21);
		comboBox.setEditable(false);
		frame.getContentPane().add(comboBox);
		
		JLabel lblCount = new JLabel("count 随机事件数");
		lblCount.setFont(new Font("宋体", Font.PLAIN, 12));
		lblCount.setBounds(326, 151, 133, 15);
		frame.getContentPane().add(lblCount);
		
		JLabel lblChongxian = new JLabel("重现种子数s");
		lblChongxian.setFont(new Font("宋体", Font.PLAIN, 12));
		lblChongxian.setBounds(326, 182, 133, 15);
		frame.getContentPane().add(lblChongxian);
		
		
		textField_23 = new JTextField();
		textField_23.setColumns(10);
		textField_23.setBounds(517, 179, 66, 21);
		frame.getContentPane().add(textField_23);
		
		textField_11 = new JTextField();
		textField_11.setBounds(517, 148, 66, 21);
		frame.getContentPane().add(textField_11);
		textField_11.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("-pct-touch 触摸事件%");
		lblNewLabel_4.setFont(new Font("宋体", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(326, 228, 152, 15);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblpctmotion = new JLabel("-pct-motion 动作事件%");
		lblpctmotion.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpctmotion.setBounds(326, 253, 152, 15);
		frame.getContentPane().add(lblpctmotion);
		
		JLabel lblpcttrackball = new JLabel("-pct-trackball 轨迹球事件%");
		lblpcttrackball.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpcttrackball.setBounds(326, 278, 184, 15);
		frame.getContentPane().add(lblpcttrackball);
		
		JLabel lblpctnav = new JLabel("-pct-nav 基本导航事件%");
		lblpctnav.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpctnav.setBounds(326, 305, 184, 15);
		frame.getContentPane().add(lblpctnav);
		
		JLabel lblpctmajornav = new JLabel("-pct-majornav 主要导航事件%");
		lblpctmajornav.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpctmajornav.setBounds(326, 334, 184, 15);
		frame.getContentPane().add(lblpctmajornav);
		
		JLabel lblpctsyskeys = new JLabel("-pct-syskeys 系统关键事件%");
		lblpctsyskeys.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpctsyskeys.setBounds(326, 359, 184, 15);
		frame.getContentPane().add(lblpctsyskeys);
		
		JLabel lblpctappswitchactivity = new JLabel("-pct-appswitch 运行包内activity%");
		lblpctappswitchactivity.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpctappswitchactivity.setBounds(326, 384, 202, 15);
		frame.getContentPane().add(lblpctappswitchactivity);
		
		JLabel lblpctanyevent = new JLabel("-pct-anyevent 其他类型事件%");
		lblpctanyevent.setFont(new Font("宋体", Font.PLAIN, 12));
		lblpctanyevent.setBounds(326, 409, 176, 15);
		frame.getContentPane().add(lblpctanyevent);
		
		textField_12 = new JTextField();
		textField_12.setBounds(543, 219, 66, 21);
		textField_12.setText("0");
		frame.getContentPane().add(textField_12);
		textField_12.setColumns(10);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setText("0");
		textField_13.setBounds(543, 244, 66, 21);
		frame.getContentPane().add(textField_13);
		
		textField_14 = new JTextField();
		textField_14.setColumns(10);
		textField_14.setText("0");
		textField_14.setBounds(543, 271, 66, 21);
		frame.getContentPane().add(textField_14);
		
		textField_15 = new JTextField();
		textField_15.setColumns(10);
		textField_15.setText("0");
		textField_15.setBounds(543, 296, 66, 21);
		frame.getContentPane().add(textField_15);
		
		textField_16 = new JTextField();
		textField_16.setColumns(10);
		textField_16.setText("0");
		textField_16.setBounds(543, 325, 66, 21);
		frame.getContentPane().add(textField_16);
		
		textField_17 = new JTextField();
		textField_17.setColumns(10);
		textField_17.setText("0");
		textField_17.setBounds(543, 350, 66, 21);
		frame.getContentPane().add(textField_17);
		
		textField_18 = new JTextField();
		textField_18.setColumns(10);
		textField_18.setText("0");
		textField_18.setBounds(543, 381, 66, 21);
		frame.getContentPane().add(textField_18);
		
		textField_19 = new JTextField();
		textField_19.setColumns(10);
		textField_19.setText("0");
		textField_19.setBounds(543, 407, 66, 21);
		frame.getContentPane().add(textField_19);
		
		JButton button_3 = new JButton("查看日志");
		button_3.setFont(new Font("宋体", Font.PLAIN, 12));
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File("./日志");
				/*File file2 = new File(System.getProperty("user.dir"));
				String filePath = System.getProperty("user.dir");
				
				System.out.println(file2);*/
				try {
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_3.setBounds(503, 468, 93, 23);
		frame.getContentPane().add(button_3);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setFont(new Font("宋体", Font.ITALIC, 12));
		panel_2.setBorder(BorderFactory.createTitledBorder("获取应用信息"));
		panel_2.setBounds(14, 330, 292, 230);
		frame.getContentPane().add(panel_2);
		
		JButton btnNewButton_3 = new JButton("统计异常");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file2 = new File("./日志");
				
				String bat ="cmd.exe /c ";
				String bat1=System.getProperty("user.dir");
				System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径
				System.out.println(bat);
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec(bat+bat1+"\\count.bat");
					System.out.println(bat+bat1+"\\count.bat");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_3.setFont(new Font("宋体", Font.ITALIC, 12));
		btnNewButton_3.setBounds(340, 508, 93, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		textField_20 = new JTextField();
		textField_20.setBounds(489, 509, 66, 21);
		frame.getContentPane().add(textField_20);
		textField_20.setColumns(10);
		
		textField_21 = new JTextField();
		textField_21.setBounds(601, 509, 66, 21);
		frame.getContentPane().add(textField_21);
		textField_21.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("ANR");
		lblNewLabel_1.setFont(new Font("宋体", Font.ITALIC, 12));
		lblNewLabel_1.setBounds(456, 512, 54, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel label_7 = new JLabel("崩溃");
		label_7.setFont(new Font("宋体", Font.ITALIC, 12));
		label_7.setBounds(565, 512, 54, 15);
		frame.getContentPane().add(label_7);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setFont(new Font("宋体", Font.ITALIC, 12));
		panel.setBorder(BorderFactory.createTitledBorder("Monkey"));
		panel.setBounds(311, 35, 395, 525);
		frame.getContentPane().add(panel);
		
		/*JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(117, 223, 146, 23);
		progressBar.setOrientation(JProgressBar.HORIZONTAL);
	    //progressBar.setSize(200, 100);
	    progressBar.setMinimum(0);
	    progressBar.setMaximum(100);
	    progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);
		frame.add(progressBar);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setBounds(117, 284, 146, 23);
		progressBar_1.setOrientation(JProgressBar.HORIZONTAL);
	    //progressBar.setSize(200, 100);
	    progressBar_1.setMinimum(0);
	    progressBar_1.setMaximum(100);
	    progressBar_1.setStringPainted(true);
		frame.getContentPane().add(progressBar_1);*/
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(BorderFactory.createTitledBorder("安装卸载测试"));
		panel_1.setFont(new Font("宋体", Font.ITALIC, 12));
		panel_1.setBounds(12, 156, 280, 164);
		frame.getContentPane().add(panel_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String select = (String) comboBox.getSelectedItem();
				String level = null;
				File file2 = new File("./日志");
				if(select=="0"){
					level= " -v ";
				}else if (select=="1") {
					level = " -v -v ";
				}else {
					level = " -v -v -v ";
				}
				String install = "cmd.exe /c adb shell monkey ";
				try {
					Runtime rt = Runtime.getRuntime();
					rt.exec(install+"-p "+textField_9.getText()+" --ignore-crashes --ignore-timeouts"+
							" -s "+textField_23.getText()+" --throttle "+textField_10.getText()+
							" --pct-touch "+textField_12.getText()+" --pct-motion "+textField_13.getText()
							+" --pct-trackball "+textField_14.getText()+" --pct-nav "+textField_15.getText()+"  --pct-majornav "
							+textField_16.getText()+" --pct-syskeys "+textField_17.getText()+" --pct-appswitch "+textField_18.getText()
							+" --pct-anyevent "+textField_19.getText()+ level+""+textField_11.getText()+" 1>"+file2+"\\success.txt"+" 2>"+file2+"\\error.txt");
					System.out.println(install+"-p "+textField_9.getText()+" --ignore-crashes --ignore-timeouts"+
							" -s "+textField_23.getText()+" --throttle "+textField_10.getText()+
							" --pct-touch "+textField_12.getText()+" --pct-motion "+textField_13.getText()
							+" --pct-trackball "+textField_14.getText()+" --pct-nav "+textField_15.getText()+"  --pct-majornav "
							+textField_16.getText()+" --pct-syskeys "+textField_17.getText()+" --pct-appswitch "+textField_18.getText()
							+" --pct-anyevent "+textField_19.getText()+ level+""+textField_11.getText()+" 1>"+file2+"\\monkey.txt"+" 2>"+file2+"\\error.txt");
					}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
	}
}
