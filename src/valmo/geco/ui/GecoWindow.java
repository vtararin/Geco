/**
 * Copyright (c) 2009 Simon Denier
 */
package valmo.geco.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import valmo.geco.core.Announcer;
import valmo.geco.core.Geco;
import valmo.geco.model.RunnerRaceData;
import valmo.geco.model.Stage;
import valmo.geco.model.Status;


/**
 * GecoWindow is the main frame of the application and primarily responsible for initializing the main
 * GecoPanel in tabs. It also takes care of the main toolbar.
 * 
 * @author Simon Denier
 * @since Jan 23, 2009
 */
public class GecoWindow extends JFrame implements Announcer.StageListener {
	
	private Geco geco;

	private StagePanel stagePanel;
	
	private RunnersPanel runnersPanel;
	
	private LivePanel livePanel;
	
	private ResultsPanel resultsPanel;

	private HeatsPanel heatsPanel;

	
	/**
	 * 
	 */
	public GecoWindow(Geco geco, Announcer announcer) {
		this.geco = geco;
		this.stagePanel = new StagePanel(this.geco, this, announcer);
		this.runnersPanel = new RunnersPanel(this.geco, this, announcer);
		this.resultsPanel = new ResultsPanel(this.geco, this, announcer);
		this.livePanel = new LivePanel(this.geco, this, announcer);
		this.heatsPanel = new HeatsPanel(this.geco, this, announcer);
		announcer.registerStageListener(this);
		guiInit();
	}
	
	public void guiInit() {
		setTitle("Geco - " + geco.stage().getName());
		getContentPane().add(initToolbar(), BorderLayout.NORTH);
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Stage", this.stagePanel);
		pane.addTab("Runners", this.runnersPanel);
		pane.addTab("Live", this.livePanel);
		pane.addTab("Results", this.resultsPanel);
		pane.addTab("Heats", this.heatsPanel);
		getContentPane().add(pane, BorderLayout.CENTER);

//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				geco.exit();
			}
		});
	}

	public void launchGUI() {
		setVisible(true);
		pack();
	}

	private JToolBar initToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JButton importB = new JButton("import");
		importB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geco.importStage();
			}
		});
		toolBar.add(importB);
		JButton saveB = new JButton("save");
		toolBar.add(saveB);
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geco.saveCurrentStage();
			}
		});
		toolBar.addSeparator();
		JButton previousB = new JButton("previous stage");
		previousB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geco.switchToPreviousStage();
			}
		});
		toolBar.add(previousB);
		JButton nextB = new JButton("next stage");
		nextB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geco.switchToNextStage();
			}
		});
		toolBar.add(nextB);
		toolBar.addSeparator();
		JButton statusB = new JButton("Recheck All");
		statusB.setToolTipText("Recheck all OK/MP to update statuses");
		statusB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (RunnerRaceData data: geco.registry().getRunnersData()) {
					if( data.getResult().getStatus().equals(Status.OK) 
							|| data.getResult().getStatus().equals(Status.MP) ) {
						geco.checker().check(data);
					}
				}
				geco.logger().log("Recheck all OK|MP data");
			}
		});
		toolBar.add(statusB);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(new JLabel("Crawling for Better Orienteering Software - v0.1"));
		return toolBar;
	}

	@Override
	public void changed(Stage previous, Stage next) {
		setTitle("Geco - " + geco.stage().getName());
//		this.stagePanel.refresh();
//		repaint();
	}

	@Override
	public void saving(Stage stage, Properties properties) {
	}

	@Override
	public void closing(Stage stage) {
	}


}
