/**
 * Copyright (c) 2011 Simon Denier
 * Released under the MIT License (see LICENSE file)
 */
package net.geco.ui.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.geco.basics.Announcer;
import net.geco.framework.IGecoApp;
import net.geco.model.Category;
import net.geco.model.Messages;
import net.geco.ui.framework.ConfigPanel;

/**
 * @author Simon Denier
 * @since May 25, 2011
 *
 */
public class CategoryConfigPanel extends ConfigTablePanel<Category> implements ConfigPanel {

	private ConfigTableModel<Category> tableModel;
	private JComboBox coursesCB;

	@Override
	public String getLabel() {
		return Messages.uiGet("StagePanel.CategoryConfigTitle"); //$NON-NLS-1$
	}

	public CategoryConfigPanel(final IGecoApp geco, final JFrame frame) {
		coursesCB = new JComboBox();
		
		tableModel = createTableModel(geco);
		tableModel.setData(geco.registry().getSortedCategories());
		
		geco.announcer().registerStageConfigListener( new Announcer.StageConfigListener() {
			public void coursesChanged() {
				updateCoursesCBforCategories(geco, coursesCB);
			}
			public void clubsChanged() {}
			public void categoriesChanged() {
				tableModel.setData(geco.registry().getSortedCategories());
			}
		});
	
		ActionListener addAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				geco.stageControl().createCategory();
			}
		};
		ActionListener removeAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Category cat = getSelectedData();
				if( cat!=null ) {
					try {
						geco.stageControl().removeCategory(cat);	
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(frame,
								Messages.uiGet("StagePanel.CategoryNoDeletionWarning") + e2.getMessage(), //$NON-NLS-1$
								Messages.uiGet("StagePanel.ActionCancelledTitle"), //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		};

		initialize(
				Messages.uiGet("StagePanel.CategoryConfigTitle"), //$NON-NLS-1$
				tableModel,
				addAction,
				removeAction);
		updateCoursesCBforCategories(geco, coursesCB);
		table().getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(coursesCB));
	}

	public ConfigTableModel<Category> createTableModel(final IGecoApp geco) {
		return new ConfigTableModel<Category>(new String[] {
											Messages.uiGet("StagePanel.CategoryShortNameHeader"), //$NON-NLS-1$
											Messages.uiGet("StagePanel.CategoryLongNameHeader"), //$NON-NLS-1$
											Messages.uiGet("StagePanel.CategoryCourseHeader") }) { //$NON-NLS-1$
			@Override
			public Object getValueIn(Category cat, int columnIndex) {
				switch (columnIndex) {
				case 0: return cat.getShortname();
				case 1: return cat.getLongname();
				case 2: return (cat.getCourse()==null) ? "" : cat.getCourse().getName(); //$NON-NLS-1$
				default: return super.getValueIn(cat, columnIndex);
				}
			}
			@Override
			public void setValueIn(Category cat, Object value, int col) {
				switch (col) {
				case 0: geco.stageControl().updateShortname(cat, (String) value); break;
				case 1: geco.stageControl().updateName(cat, (String) value); break;
				case 2: setCourse(cat, (String) value); break;
				default: break;
				}
			}
			private void setCourse(Category cat, String coursename) {
				if( coursename.equals("") ){ //$NON-NLS-1$
					cat.setCourse(null);
				} else {
					cat.setCourse(geco.registry().findCourse(coursename));
				}
			}
		};
	}

	private void updateCoursesCBforCategories(IGecoApp geco, JComboBox coursesCB) {
		Vector<String> sortedCoursenames = new Vector<String>( geco.registry().getSortedCoursenames() );
		sortedCoursenames.add(0, ""); //$NON-NLS-1$
		coursesCB.setModel(new DefaultComboBoxModel(sortedCoursenames));
	}
	
}
