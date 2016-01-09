package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import animation.Keyframe;
import interfaces.OnModeChangeListener;
import interfaces.OnObjectSelectedListener;
import interfaces.OnObjectUpdateListener;
import interfaces.OnTimeUpdateListener;
import managers.ModeManager;
import managers.ObjectsManager;
import managers.TimeManager;
import shapes.TransformableShape;

public class ObjectPanel extends JPanel implements OnObjectSelectedListener, OnTimeUpdateListener, ChangeListener, ActionListener, OnObjectUpdateListener, ItemListener, OnModeChangeListener{

	public static final String SELECTED="CARD_SELECTED";
	public static final String NOT_SELECTED="CARD_NOT_SELECTED";
	public static final String FREE_FORM="CARD_FREE_FORM";

	private static final String NO_PARENT_STR="[No Parent]";


	ImageIcon graphIcon, saveIcon, deleteIcon;

	JPanel selectedMenu, unselectedMenu, ffMenu;
	JPanel line1,line2,line3;
	JLabel line1_object, line2_x,line2_y,line2_scaleX,line2_scaleY,line2_rotation,line2_color,line2_parent,line2_order;
	JButton line2_x_graph,line2_y_graph,line2_scaleX_graph,line2_scaleY_graph,line2_rotation_graph;
	JButton line2_x_save,line2_y_save,line2_scaleX_save,line2_scaleY_save,line2_rotation_save;
	JButton line2_x_delete,line2_y_delete,line2_scaleX_delete,line2_scaleY_delete,line2_rotation_delete;
	JTextField line1_objectName;
	JSpinner line2_x_val,line2_y_val,line2_scaleX_val,line2_scaleY_val,line2_rotation_val,line2_order_val;
	JComboBox line2_parent_val;
	DefaultComboBoxModel<String> line2_parent_val_model;
	CardLayout layout;
	JButton line3_delete, line3_duplicate, line2_color_val, line3_ive;

	JPanel unselected_background_container;
	JButton unselected_create_triangle,unselected_create_square,unselected_create_hexagon,unselected_create_octagon,unselected_create_circle,unselected_freeform;
	JLabel unselected_background;
	JButton unselected_background_val;

	JLabel ff_label;
	JButton ff_done;

	ObjectsManager om;
	TimeManager tm;
	ModeManager mm;

	TransformableShape selected;

	GraphPanel gp;

	public void setGraphPanel(GraphPanel gp){
		this.gp=gp;
	}
	
	public ObjectPanel(ObjectsManager om, TimeManager tm, ModeManager mm){

		graphIcon=new ImageIcon("media/buttons/graph_30px.png");
		saveIcon=new ImageIcon("media/buttons/save_30px.png");
		deleteIcon=new ImageIcon("media/buttons/delete_30px.png");

		this.om=om;
		this.tm=tm;
		this.mm=mm;

		layout=new CardLayout();

		setLayout(layout);


		selectedMenu=new JPanel();
		selectedMenu.setLayout(new BoxLayout(selectedMenu,BoxLayout.Y_AXIS));

		line1=new JPanel();
		//line1.setBackground(Color.GREEN);
		//line1.setAlignmentX(Component.LEFT_ALIGNMENT);
		line1.setLayout(new BoxLayout(line1,BoxLayout.Y_AXIS));

		line1_object =new JLabel("Selected Object");
		line1_object.setAlignmentX(Component.CENTER_ALIGNMENT);
		line1.add(line1_object);

		line1_objectName=new JTextField(null);
		line1_objectName.setFont(new Font("SansSerif", Font.BOLD, 20));
		line1_objectName.addActionListener(this);
		line1_objectName.setMaximumSize(new Dimension(300,30));
		line1_objectName.setAlignmentX(Component.CENTER_ALIGNMENT);
		line1_objectName.setEditable(false);
		line1_objectName.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				line1_objectName.setEditable(true);
				line1_objectName.getCaret().setVisible(true);
			}
		});
		line1.add(line1_objectName);

		selectedMenu.add(line1);


		line2=new JPanel();
		line2.setLayout(new GridBagLayout());

		GridBagConstraints gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_x=new JLabel("X");
		line2.add(line2_x,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=0;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_x_val=new JSpinner(new SpinnerNumberModel(0.0,-1000.0 ,1000.0,1.0));
		line2_x_val.setPreferredSize(new Dimension(100,30));
		line2_x_val.addChangeListener(this);
		line2.add(line2_x_val,gc);

		gc=new GridBagConstraints();
		gc.gridx=2;
		gc.gridy=0;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_x_graph=new JButton(graphIcon);
		line2_x_graph.setPreferredSize(new Dimension(30,30));
		line2_x_graph.setMinimumSize(new Dimension(30,30));
		line2_x_graph.addActionListener(this);
		line2_x_graph.setBackground(Color.WHITE);
		line2.add(line2_x_graph,gc);

		gc=new GridBagConstraints();
		gc.gridx=3;
		gc.gridy=0;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_x_save=new JButton(saveIcon);
		line2_x_save.setPreferredSize(new Dimension(30,30));
		line2_x_save.setMinimumSize(new Dimension(30,30));
		line2_x_save.addActionListener(this);
		line2_x_save.setBackground(Color.WHITE);
		line2.add(line2_x_save,gc);

		gc=new GridBagConstraints();
		gc.gridx=4;
		gc.gridy=0;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_x_delete=new JButton(deleteIcon);
		line2_x_delete.setPreferredSize(new Dimension(30,30));
		line2_x_delete.setMinimumSize(new Dimension(30,30));
		line2_x_delete.addActionListener(this);
		line2_x_delete.setBackground(Color.WHITE);
		line2.add(line2_x_delete,gc);


		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=1;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_y=new JLabel("Y");
		line2.add(line2_y,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=1;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_y_val=new JSpinner(new SpinnerNumberModel(0.0,-1000.0 ,1000.0,1.0));
		line2_y_val.setPreferredSize(new Dimension(100,30));
		line2_y_val.addChangeListener(this);
		line2.add(line2_y_val,gc);

		gc=new GridBagConstraints();
		gc.gridx=2;
		gc.gridy=1;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_y_graph=new JButton(graphIcon);
		line2_y_graph.setPreferredSize(new Dimension(30,30));
		line2_y_graph.setMinimumSize(new Dimension(30,30));
		line2_y_graph.addActionListener(this);
		line2_y_graph.setBackground(Color.WHITE);
		line2.add(line2_y_graph,gc);

		gc=new GridBagConstraints();
		gc.gridx=3;
		gc.gridy=1;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_y_save=new JButton(saveIcon);
		line2_y_save.setPreferredSize(new Dimension(30,30));
		line2_y_save.setMinimumSize(new Dimension(30,30));
		line2_y_save.addActionListener(this);
		line2_y_save.setBackground(Color.WHITE);
		line2.add(line2_y_save,gc);

		gc=new GridBagConstraints();
		gc.gridx=4;
		gc.gridy=1;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_y_delete=new JButton(deleteIcon);
		line2_y_delete.setPreferredSize(new Dimension(30,30));
		line2_y_delete.setMinimumSize(new Dimension(30,30));
		line2_y_delete.addActionListener(this);
		line2_y_delete.setBackground(Color.WHITE);
		line2.add(line2_y_delete,gc);


		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=2;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_scaleX=new JLabel("Sx");
		line2.add(line2_scaleX,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=2;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleX_val=new JSpinner(new SpinnerNumberModel(0.0,-1000.0 ,1000.0,0.01));
		line2_scaleX_val.setPreferredSize(new Dimension(100,30));
		line2_scaleX_val.addChangeListener(this);
		line2.add(line2_scaleX_val,gc);

		gc=new GridBagConstraints();
		gc.gridx=2;
		gc.gridy=2;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleX_graph=new JButton(graphIcon);
		line2_scaleX_graph.setPreferredSize(new Dimension(30,30));
		line2_scaleX_graph.setMinimumSize(new Dimension(30,30));
		line2_scaleX_graph.addActionListener(this);
		line2_scaleX_graph.setBackground(Color.WHITE);
		line2.add(line2_scaleX_graph,gc);

		gc=new GridBagConstraints();
		gc.gridx=3;
		gc.gridy=2;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleX_save=new JButton(saveIcon);
		line2_scaleX_save.setPreferredSize(new Dimension(30,30));
		line2_scaleX_save.setMinimumSize(new Dimension(30,30));
		line2_scaleX_save.addActionListener(this);
		line2_scaleX_save.setBackground(Color.WHITE);
		line2.add(line2_scaleX_save,gc);

		gc=new GridBagConstraints();
		gc.gridx=4;
		gc.gridy=2;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleX_delete=new JButton(deleteIcon);
		line2_scaleX_delete.setPreferredSize(new Dimension(30,30));
		line2_scaleX_delete.setMinimumSize(new Dimension(30,30));
		line2_scaleX_delete.addActionListener(this);
		line2_scaleX_delete.setBackground(Color.WHITE);
		line2.add(line2_scaleX_delete,gc);


		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=3;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_scaleY=new JLabel("Sy");
		line2.add(line2_scaleY,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=3;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleY_val=new JSpinner(new SpinnerNumberModel(0.0,-1000.0 ,1000.0,0.01));
		line2_scaleY_val.setPreferredSize(new Dimension(100,30));
		line2_scaleY_val.addChangeListener(this);
		line2.add(line2_scaleY_val,gc);

		gc=new GridBagConstraints();
		gc.gridx=2;
		gc.gridy=3;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleY_graph=new JButton(graphIcon);
		line2_scaleY_graph.setPreferredSize(new Dimension(30,30));
		line2_scaleY_graph.setMinimumSize(new Dimension(30,30));
		line2_scaleY_graph.addActionListener(this);
		line2_scaleY_graph.setBackground(Color.WHITE);
		line2.add(line2_scaleY_graph,gc);

		gc=new GridBagConstraints();
		gc.gridx=3;
		gc.gridy=3;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleY_save=new JButton(saveIcon);
		line2_scaleY_save.setPreferredSize(new Dimension(30,30));
		line2_scaleY_save.setMinimumSize(new Dimension(30,30));
		line2_scaleY_save.addActionListener(this);
		line2_scaleY_save.setBackground(Color.WHITE);
		line2.add(line2_scaleY_save,gc);

		gc=new GridBagConstraints();
		gc.gridx=4;
		gc.gridy=3;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_scaleY_delete=new JButton(deleteIcon);
		line2_scaleY_delete.setPreferredSize(new Dimension(30,30));
		line2_scaleY_delete.setMinimumSize(new Dimension(30,30));
		line2_scaleY_delete.addActionListener(this);
		line2_scaleY_delete.setBackground(Color.WHITE);
		line2.add(line2_scaleY_delete,gc);

		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=4;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_rotation=new JLabel("Rotation");
		line2.add(line2_rotation,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=4;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_rotation_val=new JSpinner(new SpinnerNumberModel(0.0,-1000.0 ,1000.0,1.0));
		line2_rotation_val.setPreferredSize(new Dimension(100,30));
		line2_rotation_val.addChangeListener(this);
		line2.add(line2_rotation_val,gc);

		gc=new GridBagConstraints();
		gc.gridx=2;
		gc.gridy=4;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_rotation_graph=new JButton(graphIcon);
		line2_rotation_graph.setPreferredSize(new Dimension(30,30));
		line2_rotation_graph.setMinimumSize(new Dimension(30,30));
		line2_rotation_graph.addActionListener(this);
		line2_rotation_graph.setBackground(Color.WHITE);
		line2.add(line2_rotation_graph,gc);

		gc=new GridBagConstraints();
		gc.gridx=3;
		gc.gridy=4;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_rotation_save=new JButton(saveIcon);
		line2_rotation_save.setPreferredSize(new Dimension(30,30));
		line2_rotation_save.setMinimumSize(new Dimension(30,30));
		line2_rotation_save.addActionListener(this);
		line2_rotation_save.setBackground(Color.WHITE);
		line2.add(line2_rotation_save,gc);

		gc=new GridBagConstraints();
		gc.gridx=4;
		gc.gridy=4;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_rotation_delete=new JButton(deleteIcon);
		line2_rotation_delete.setPreferredSize(new Dimension(30,30));
		line2_rotation_delete.setMinimumSize(new Dimension(30,30));
		line2_rotation_delete.addActionListener(this);
		line2_rotation_delete.setBackground(Color.WHITE);
		line2.add(line2_rotation_delete,gc);


		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=5;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_color=new JLabel("Color");
		line2.add(line2_color,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=5;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_color_val=new JButton();
		line2_color_val.setPreferredSize(new Dimension(30,30));
		line2_color_val.setMinimumSize(new Dimension(30,30));
		line2_color_val.addActionListener(this);
		line2.add(line2_color_val,gc);


		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=6;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_parent=new JLabel("Parent");
		line2.add(line2_parent,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=6;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_parent_val_model=new DefaultComboBoxModel<>();
		line2_parent_val=new JComboBox();
		line2_parent_val.setModel(line2_parent_val_model);
		line2_parent_val.addItemListener(this);
		line2.add(line2_parent_val,gc);

		gc=new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=7;
		gc.anchor=GridBagConstraints.LINE_END;
		line2_order=new JLabel("Order");
		line2.add(line2_order,gc);

		gc=new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=7;
		gc.anchor=GridBagConstraints.LINE_START;
		line2_order_val=new JSpinner(new SpinnerNumberModel(0,-10,10,1));
		line2_order_val.setPreferredSize(new Dimension(100,30));
		line2_order_val.addChangeListener(this);
		line2.add(line2_order_val,gc);

		selectedMenu.add(line2);

		line3_ive=new JButton("Vertex Edit");
		line3_ive.addActionListener(this);
		line3_ive.setAlignmentX(0.5f);
		selectedMenu.add(line3_ive);


		line3=new JPanel();
		line3.setAlignmentX(0.5f);
		line3.setLayout(new FlowLayout(FlowLayout.LEADING));

		line3_delete=new JButton("Delete Object");
		line3.add(line3_delete);
		line3_delete.addActionListener(this);

		line3_duplicate=new JButton("Duplicate Object");
		line3.add(line3_duplicate);
		line3_duplicate.addActionListener(this);

		selectedMenu.add(line3);


		add(selectedMenu,SELECTED);



		unselectedMenu=new JPanel();
		unselectedMenu.setLayout(new BoxLayout(unselectedMenu,BoxLayout.Y_AXIS));

		unselected_create_triangle =new JButton("Create new triangle");
		unselected_create_triangle.addActionListener(this);
		unselected_create_triangle.setAlignmentX(0.5f);
		unselectedMenu.add(unselected_create_triangle);

		unselected_create_square =new JButton("Create new square");
		unselected_create_square.addActionListener(this);
		unselected_create_square.setAlignmentX(0.5f);
		unselectedMenu.add(unselected_create_square);

		unselected_create_hexagon =new JButton("Create new hexagon");
		unselected_create_hexagon.addActionListener(this);
		unselected_create_hexagon.setAlignmentX(0.5f);
		unselectedMenu.add(unselected_create_hexagon);

		unselected_create_octagon =new JButton("Create new octagon");
		unselected_create_octagon.addActionListener(this);
		unselected_create_octagon.setAlignmentX(0.5f);
		unselectedMenu.add(unselected_create_octagon);

		unselected_create_circle =new JButton("Create new circle");
		unselected_create_circle.addActionListener(this);
		unselected_create_circle.setAlignmentX(0.5f);
		unselectedMenu.add(unselected_create_circle);

		unselected_freeform=new JButton("Free Form Shape");
		unselected_freeform.addActionListener(this);
		unselected_freeform.setAlignmentX(0.5f);
		unselectedMenu.add(unselected_freeform);

		unselected_background_container=new JPanel();
		unselected_background_container.setAlignmentX(0.5f);
		unselected_background=new JLabel("Background Color");
		unselected_background_container.add(unselected_background);

		unselected_background_val=new JButton();
		unselected_background_val.setPreferredSize(new Dimension(30,30));
		unselected_background_val.setMinimumSize(new Dimension(30,30));
		unselected_background_val.addActionListener(this);
		unselected_background_container.add(unselected_background_val);

		unselectedMenu.add(unselected_background_container);


		add(unselectedMenu,NOT_SELECTED);


		ffMenu=new JPanel();
		ffMenu.setLayout(new BoxLayout(ffMenu,BoxLayout.Y_AXIS));

		ff_label=new JLabel("Free Form Mode.");
		ff_label.setAlignmentX(0.5f);
		ffMenu.add(ff_label);

		ff_done=new JButton("Done");
		ff_done.addActionListener(this);
		ff_done.setAlignmentX(0.5f);
		ffMenu.add(ff_done);

		add(ffMenu,FREE_FORM);

		updateInformation();
	}

	public void updateInformation(){
		if (selected!=null) {
			line1_objectName.setText(selected.getName());
			line2_x_val.setValue(selected.getKeyFramedX().getValue());
			line2_y_val.setValue(selected.getKeyFramedY().getValue());
			line2_scaleX_val.setValue(selected.getKeyFramedScaleX().getValue());
			line2_scaleY_val.setValue(selected.getKeyFramedScaleY().getValue());
			line2_rotation_val.setValue(selected.getKeyFramedRotation().getValue());
			line2_color_val.setBackground(selected.getColor());
			line2_order_val.setValue(selected.getRenderOrder());
			if (selected.hasAParent()){
				line2_parent_val.setSelectedItem(selected.getParent().getName());
			}else{
				line2_parent_val.setSelectedItem(NO_PARENT_STR);
			}
		}else{
			unselected_background_val.setBackground(om.getCanvasColor());
		}
	}

	private void initializeParentDropdown(){
		line2_parent_val.removeItemListener(this); //ItemListener fires when adding items to model, so we temporarily remove the listener.
		line2_parent_val_model.removeAllElements();
		line2_parent_val_model.addElement(NO_PARENT_STR);
		for (int i = 0; i < om.getNumOfObjects(); i++) {
			if (om.getObject(i).getName().equals(selected.getName())) continue;
			line2_parent_val_model.addElement(om.getObject(i).getName());
		}
		if (selected.hasAParent()){
			line2_parent_val.setSelectedItem(selected.getParent().getName());
		}else{
			line2_parent_val.setSelectedItem(NO_PARENT_STR);
		}
		line2_parent_val.addItemListener(this);
	}


	@Override
	public void onSelect(TransformableShape shape) {
		selected=shape;
		if (shape!=null) {
			layout.show(this,SELECTED);

			updateInformation();
			initializeParentDropdown();
		}
		else layout.show(this,NOT_SELECTED);

	}

	@Override
	public void updateTime() {
		updateInformation();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource()==line2_x_val){
			selected.getKeyFramedX().setValue((double)line2_x_val.getValue());
		}else if (e.getSource()==line2_y_val){
			selected.getKeyFramedY().setValue((double)line2_y_val.getValue());
		}else if (e.getSource()==line2_scaleX_val){
			selected.getKeyFramedScaleX().setValue((double)line2_scaleX_val.getValue());
		}else if (e.getSource()==line2_scaleY_val){
			selected.getKeyFramedScaleY().setValue((double)line2_scaleY_val.getValue());
		}else if (e.getSource()==line2_rotation_val){
			selected.getKeyFramedRotation().setValue((double)line2_rotation_val.getValue());
		}else if (e.getSource()==line2_order_val){
			selected.setRenderOrder((Integer) line2_order_val.getValue());
		}
		om.notifyOnObjectUpdateListeners();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==line3_delete){
			om.deleteObject(selected);
			onSelect(null);
		}else if (e.getSource()== unselected_create_triangle){
			TransformableShape newShape=om.createShape();
			newShape.makePolygon(3,50);
			newShape.transform(100,100,1,1,0);
		}else if (e.getSource()== unselected_create_square){
			TransformableShape newShape=om.createShape();
			newShape.makePolygon(4,50);
			newShape.transform(100,100,1,1,0);
		}else if (e.getSource()== unselected_create_hexagon){
			TransformableShape newShape=om.createShape();
			newShape.makePolygon(6,50);
			newShape.transform(100,100,1,1,0);
		}else if (e.getSource()== unselected_create_octagon){
			TransformableShape newShape=om.createShape();
			newShape.makePolygon(8,50);
			newShape.transform(100,100,1,1,0);
		}else if (e.getSource()== unselected_create_circle){
			TransformableShape newShape=om.createShape();
			newShape.makePolygon(64,50);
			newShape.transform(100,100,1,1,0);
		}else if (e.getSource()== line1_objectName){
			selected.setName(line1_objectName.getText());
			line1_objectName.setEditable(false);

		}else if (e.getSource()== line2_color_val){
			Color c=JColorChooser.showDialog(null,selected.getName()+" Color",selected.getColor());
			if (c!=null){
				selected.setColor(c);
				updateInformation();
			}
		}else if (e.getSource()== line2_x_graph){
			gp.enter(selected,GraphPanel.X);
		}else if (e.getSource()== line2_y_graph){
			gp.enter(selected,GraphPanel.Y);
		}else if (e.getSource()== line2_scaleX_graph){
			gp.enter(selected,GraphPanel.SCALE_X);
		}else if (e.getSource()== line2_scaleY_graph){
			gp.enter(selected,GraphPanel.SCALE_Y);
		}else if (e.getSource()== line2_rotation_graph){
			gp.enter(selected,GraphPanel.ROTATION);
		}else if (e.getSource()== line2_x_save){
			selected.getKeyFramedX().addKeyframe(tm.getCurrentAnimationTime());
		}else if (e.getSource()== line2_y_save){
			selected.getKeyFramedY().addKeyframe(tm.getCurrentAnimationTime());
		}else if (e.getSource()== line2_scaleX_save){
			selected.getKeyFramedScaleX().addKeyframe(tm.getCurrentAnimationTime());
		}else if (e.getSource()== line2_scaleY_save){
			selected.getKeyFramedScaleY().addKeyframe(tm.getCurrentAnimationTime());
		}else if (e.getSource()== line2_rotation_save){
			selected.getKeyFramedRotation().addKeyframe(tm.getCurrentAnimationTime());
		}else if (e.getSource()== line2_x_delete){
			selected.getKeyFramedX().clearKeyframes();
		}else if (e.getSource()== line2_y_delete){
			selected.getKeyFramedY().clearKeyframes();
		}else if (e.getSource()== line2_scaleX_delete){
			selected.getKeyFramedScaleX().clearKeyframes();
		}else if (e.getSource()== line2_scaleY_delete){
			selected.getKeyFramedScaleY().clearKeyframes();
		}else if (e.getSource()== line2_rotation_delete){
			selected.getKeyFramedRotation().clearKeyframes();
		}else if (e.getSource()== line3_ive){
			if (om.getMode()==ObjectsManager.VERTEX_EDITING){
				om.exitIVE();
				line3_ive.setText("Vertex Edit");
			}
			else if (om.getMode()==ObjectsManager.NORMAL_EDITING){
				om.enterIVE(selected);
				line3_ive.setText("Exit Vertex Edit Mode");
			}else{
				System.out.println("INVALID MODE");
			}

		}else if (e.getSource()==unselected_freeform){
			if (om.getMode()==ObjectsManager.NOTHING_SELECTED) {
				om.enterFreeForm();
				layout.show(this, FREE_FORM);
			}else{
				System.out.println("INVALID MODE");
			}

		}else if (e.getSource()==ff_done){
			if (om.getMode()==ObjectsManager.FREE_FORM_EDITING){
			om.exitFreeForm();
			layout.show(this,SELECTED);
			}else{
				System.out.println("INVALID MODE");
			}
		}else if(e.getSource()==line3_duplicate){
			om.duplicateShape(selected);
		}else if (e.getSource()== unselected_background_val){
			Color c=JColorChooser.showDialog(null,"Canvas Color",om.getCanvasColor());
			if (c!=null){
				om.setCanvasColor(c);
				updateInformation();
			}
		}else{
			System.out.println("Unhandled Action.");
		}
		om.notifyOnObjectUpdateListeners();
	}

	@Override
	public void updateObject() {
		updateInformation();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange()==ItemEvent.SELECTED) {
			if (e.getItem().toString().equals(NO_PARENT_STR)) selected.clearParent();
			else selected.setParent(om.findObjectWithName(e.getItem().toString()));
			System.out.println("ItemListener11:" + e.getItem().toString());
			updateInformation();
		}
	}

	private void setNumericValueVisibility(boolean visibility){
		line2_x.setVisible(visibility);
		line2_x_val.setVisible(visibility);
		line2_y.setVisible(visibility);
		line2_y_val.setVisible(visibility);
		line2_scaleX.setVisible(visibility);
		line2_scaleX_val.setVisible(visibility);
		line2_scaleY.setVisible(visibility);
		line2_scaleY_val.setVisible(visibility);
		line2_rotation.setVisible(visibility);
		line2_rotation_val.setVisible(visibility);
	}
	private void setGraphVisibility(boolean visibility){
		line2_x_graph.setVisible(visibility);
		line2_y_graph.setVisible(visibility);
		line2_scaleX_graph.setVisible(visibility);
		line2_scaleY_graph.setVisible(visibility);
		line2_rotation_graph.setVisible(visibility);
	}
	private void setKeyframesVisibility(boolean visibility){
		line2_x_save.setVisible(visibility);
		line2_y_save.setVisible(visibility);
		line2_scaleX_save.setVisible(visibility);
		line2_scaleY_save.setVisible(visibility);
		line2_rotation_save.setVisible(visibility);

		line2_x_delete.setVisible(visibility);
		line2_y_delete.setVisible(visibility);
		line2_scaleX_delete.setVisible(visibility);
		line2_scaleY_delete.setVisible(visibility);
		line2_rotation_delete.setVisible(visibility);
	}
	private void setColorVisibility(boolean visibility){
		line2_color.setVisible(visibility);
		line2_color_val.setVisible(visibility);
	}
	private void setParentingVisibility(boolean visibility){
		line2_parent.setVisible(visibility);
		line2_parent_val.setVisible(visibility);
	}
	private void setOrderingVisibility(boolean visibility){
		line2_order.setVisible(visibility);
		line2_order_val.setVisible(visibility);
	}
	private void setVertexEditVisibility(boolean visibility){
		line3_ive.setVisible(visibility);
	}
	private void setFreeFormVisibility(boolean visibility){
		unselected_freeform.setVisible(visibility);
	}

	@Override
	public void modeChanged(int newMode) {
		setNumericValueVisibility(false);
		setGraphVisibility(false);
		setKeyframesVisibility(false);
		//setColorVisibility(false);
		setParentingVisibility(false);
		//setOrderingVisibility(false);
		setVertexEditVisibility(false);
		setFreeFormVisibility(false);

		if (mm.higherThanOET(ModeManager.INTERMEDIATE)){
			setNumericValueVisibility(true);
			setKeyframesVisibility(true);

		}
		if (mm.higherThanOET(ModeManager.ADVANCED)){
			setParentingVisibility(true);
			setFreeFormVisibility(true);
			setVertexEditVisibility(true);
		}
		if (mm.higherThanOET(ModeManager.EXPERT)){
			setGraphVisibility(true);
		}
	}
}
