package com.gennadz;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.ScrollPane;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.JSONException;
import org.json.JSONObject;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
	public static final int DEFAULT_WIDTH_PANEL = 500;
	public static final int DEFAULT_HEIGHT_PANEL = 200;
	public static final int DEFAULT_WIDTH_TEXTAREA = 200;
	public static final int DEFAULT_HEIGHT_TEXTAREA = 200;
	public static final int DEFAULT_WIDTH_FRAME = 200;
	public static final int DEFAULT_HEIGHT_FRAME = 200;
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;
	JTextArea inputArea;
	JTextArea outputArea;
	
	public MainFrame() throws IOException {
		setTitle("This is header");
		setSize(416, 374);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu_2 = new JMenu("Файл");
		menuBar.add(menu_2);
		
		JMenu menu_3 = new JMenu("Выход");
		menu_2.add(menu_3);
		
		JMenu menu = new JMenu("Помощь");
		menuBar.add(menu);
		
		JMenu menu_1 = new JMenu("О программе");
		menu.add(menu_1);
		getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		});
		textArea.setBounds(70, 55, 237, 63);
		getContentPane().add(textArea);
		
		JLabel label = new JLabel("Текст");
		label.setBounds(165, 30, 46, 14);
		getContentPane().add(label);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(70, 187, 237, 63);
		getContentPane().add(textArea_1);
		
		JLabel label_1 = new JLabel("Перевод");
		label_1.setBounds(165, 162, 46, 14);
		getContentPane().add(label_1);
		
		JButton button = new JButton("Перевести");
		button.setBounds(70, 273, 89, 23);
		getContentPane().add(button);
		
		JButton button_1 = new JButton("Очистить");
		button_1.setBounds(218, 273, 89, 23);
		getContentPane().add(button_1);
		//textareas
		inputArea = new JTextArea(8,20);
		JScrollPane inputScrollPane = new JScrollPane(inputArea);
		outputArea = new JTextArea(8,20);
		JScrollPane outputScrollPane = new JScrollPane(outputArea);
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
}