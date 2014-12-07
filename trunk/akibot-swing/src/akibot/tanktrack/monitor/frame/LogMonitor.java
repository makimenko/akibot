package akibot.tanktrack.monitor.frame;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.TextArea;
import javax.swing.JPanel;
import java.awt.Scrollbar;

public class LogMonitor extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogMonitor frame = new LogMonitor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LogMonitor() {
		setTitle("Log Monitor");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setBounds(100, 100, 608, 404);
		
		TextArea textArea = new TextArea();
		getContentPane().add(textArea, BorderLayout.CENTER);

	}

}
