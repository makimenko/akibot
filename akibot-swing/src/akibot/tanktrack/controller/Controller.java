package akibot.tanktrack.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Controller {

	private JFrame mainFrame;
	private final Action exitAction = new SwingExitAction();
	private final Action cmdForward = new SwingAction_4();
	private final Action cmdBackward = new SwingAction_5();
	private final Action cmdLeft = new SwingAction_6();
	private final Action cmdRight = new SwingAction_7();
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Controller window = new Controller();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Controller() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		mainFrame.setTitle("TankTrack Controller");
		mainFrame.setBounds(100, 100, 964, 713);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAction(exitAction);
		mnFile.add(mntmExit);
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		
				JPanel panelCommands = new JPanel();
				mainFrame.getContentPane().add(panelCommands, BorderLayout.EAST);
				panelCommands.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
						JPanel panelMovement = new JPanel();
						panelCommands.add(panelMovement);
						panelMovement.setAlignmentX(Component.LEFT_ALIGNMENT);
						panelMovement.setAlignmentY(10.0f);
						panelMovement.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
						panelMovement.setLayout(new GridLayout(0, 3, 1, 1));
						
								JLabel lblSpaceLabel1 = new JLabel("");
								panelMovement.add(lblSpaceLabel1);
								
										JButton btnForward = new JButton("");
										btnForward.addChangeListener(new ChangeListener() {
											public void stateChanged(ChangeEvent e) {
												
											}
										});
										buttonGroup.add(btnForward);
										btnForward.setAction(cmdForward);
										panelMovement.add(btnForward);
										
												JLabel lblSpaceLabel2 = new JLabel("");
												panelMovement.add(lblSpaceLabel2);
												
														JButton btnLeft = new JButton("");
														buttonGroup.add(btnLeft);
														btnLeft.setAction(cmdLeft);
														panelMovement.add(btnLeft);
														
																JButton btnBackward = new JButton("");
																buttonGroup.add(btnBackward);
																btnBackward.setAction(cmdBackward);
																panelMovement.add(btnBackward);
																
																		JButton btnRight = new JButton("");
																		buttonGroup.add(btnRight);
																		btnRight.setAction(cmdRight);
																		panelMovement.add(btnRight);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panelLog = new JPanel();
		tabbedPane.addTab("Log", null, panelLog, null);

		JPanel panelClient = new JPanel();
		tabbedPane.addTab("Clients", null, panelClient, null);
	}

	private class SwingExitAction extends AbstractAction {
		public SwingExitAction() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "SwingAction_1");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "SwingAction_2");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_3 extends AbstractAction {
		public SwingAction_3() {
			putValue(NAME, "SwingAction_3");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_4 extends AbstractAction {
		public SwingAction_4() {
			putValue(NAME, "Forward");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_5 extends AbstractAction {
		public SwingAction_5() {
			putValue(NAME, "Backward");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_6 extends AbstractAction {
		public SwingAction_6() {
			putValue(NAME, "Left");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_7 extends AbstractAction {
		public SwingAction_7() {
			putValue(NAME, "Right");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
