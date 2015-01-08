package akibot.tanktrack.monitor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

public class TestFrame extends JFrame {
	private final Action exitAction = new SwingAction();
	private final Action connectAction = new SwingAction_1();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
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
	public TestFrame() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setTitle("AkiBot Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 939, 611);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mntmNewMenuItem.setAction(exitAction);
		mnFile.add(mntmNewMenuItem);

		JToolBar toolBar = new JToolBar();
		toolBar.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		tglbtnNewToggleButton.setAction(connectAction);
		toolBar.add(tglbtnNewToggleButton);

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED,
				null, null, null, null)));
		FlowLayout fl_statusPanel = (FlowLayout) statusPanel.getLayout();
		fl_statusPanel.setVgap(1);
		fl_statusPanel.setHgap(1);
		fl_statusPanel.setAlignment(FlowLayout.LEFT);
		getContentPane().add(statusPanel, BorderLayout.SOUTH);

		JLabel statusLabel = new JLabel("Ready");
		statusPanel.add(statusLabel);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.control);
		getContentPane().add(desktopPane, BorderLayout.CENTER);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(LONG_DESCRIPTION, "Exit");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
			putValue(ACTION_COMMAND_KEY, "exit");
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase("exit")) {
				System.exit(0);
			}
		}
	}

	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Connect");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}
