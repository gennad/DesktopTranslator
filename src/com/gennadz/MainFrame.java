package com.gennadz;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.json.JSONException;
import org.json.JSONObject;

public class MainFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 200;
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;
	JTextArea inputArea;
	JTextArea outputArea;
	
	public MainFrame() throws IOException {
		setTitle("This is header");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//buttons
		JButton siteButton = new JButton("Open the site");
		//textareas
		inputArea = new JTextArea(5,8);
		outputArea = new JTextArea(5,8);
		//adding panel to frame
		MainPanel panel = new MainPanel();
		panel.add(siteButton);
		panel.add(inputArea);
		panel.add(outputArea);
		add(panel);
		//creating action to button
		TranslateAction openSiteAction = new TranslateAction();
		siteButton.addActionListener(openSiteAction);
		//tray icon
		trayIcon = new TrayIcon(ImageIO.read(new File("trayicon.gif")), "Tray test");
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
				setState(JFrame.NORMAL);
				removeTrayIcon();
			}
	    });
		addWindowStateListener(new WindowStateListener()
	    {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if(e.getNewState() == JFrame.ICONIFIED) {
					setVisible(false);
					addTrayIcon();
				}
			}
	    });
		//add popup menu to tray
		PopupMenu popupMenu = new PopupMenu();
	    MenuItem item = new MenuItem("Exit");
	    item.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		dispose();
	    		System.exit(0);
	    	}
	    });
	    popupMenu.add(item);
	    trayIcon.setPopupMenu(popupMenu);
	}
	
	private void removeTrayIcon() {
		systemTray.remove(trayIcon);
	}
	private void addTrayIcon() {
		try {
			systemTray.add(trayIcon);
			trayIcon.displayMessage("Tray test", "Window minimised to tray, double click to show", TrayIcon.MessageType.INFO);
	    }
	    catch(AWTException ex) {
	      ex.printStackTrace();
	    }
	}
	
	class TranslateAction implements ActionListener {
		
		Logger logger = Logger.getLogger("TranslateAction");
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			URL url;
			try {
				String key = "ABQIAAAAs-s2Ae6qDIM0-Yz2G-gMxRSNnevTn-a6jTi9i5iCCE6iZiia3RRPhRaw3OofMoJY_16OF5wYTGOEQA";
				InetAddress ownIP=InetAddress.getLocalHost();
				String ip = ownIP.getHostAddress();
				String textToTranslate = inputArea.getText();
				//detect latin
				Pattern pattern = Pattern.compile("[A-Z][a-z]");
				Matcher matcher = pattern.matcher(textToTranslate);
				boolean isLatin = matcher.find();
				logger.info("isLatin:"+isLatin);
				//encode
				textToTranslate = URLEncoder.encode(textToTranslate);
				
				String langPair;
				if (isLatin) {
					langPair = "en|ru";
					langPair = URLEncoder.encode(langPair);
				} else {
					langPair = "ru|en";
					langPair = URLEncoder.encode(langPair);
					//%7C
				}
				String urlPath = "https://ajax.googleapis.com/ajax/services/language/translate?" +
				"v=1.0&q="+textToTranslate+"&langpair="+langPair+"&key="+key+"&userip="+ip;
				logger.info("urlPath: "+urlPath);
				url = new URL(urlPath);
				URLConnection connection = url.openConnection();
				connection.addRequestProperty("Referer", "http://gennadz.blogspot.com");
				String line;
				StringBuilder builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					builder.append(line);
					System.out.println(line);
				}
				JSONObject json = new JSONObject(builder.toString());
				//String[] s = JSONObject.getNames(json);
				JSONObject responseData = (JSONObject) json.get("responseData");
				String translatedText = responseData.getString("translatedText");
				
				outputArea.setText(translatedText);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
					
		}
	}
}