package managers;


/*
<<BASIC>>
>Object Manipulation(Scale)
>Basic Object Creation
>Object Deletion/Duplication
>Color Edit
>Ordering
<<INTERMEDIATE>>
>Numerical Object Manipulation
>Timeline
>Keyframe creation
<<ADVANCED>>
>Parenting
>Vertex Editing
>FreeForm Creation
<<EXPERT>>
>Graph Editing
*/


import interfaces.OnModeChangeListener;

import java.util.ArrayList;
import java.util.List;

public class ModeManager {
	static ModeManager inst=new ModeManager();
	public synchronized static ModeManager getInstance(){
		return inst;
	}
	private ModeManager(){}
	
	
	public static final int BEGINNER=0;
	public static final int INTERMEDIATE=1;
	public static final int ADVANCED=2;
	public static final int EXPERT=3;

	
	int mode=EXPERT;
	List<OnModeChangeListener> omcls=new ArrayList<>();

	public void addOnModeChangeListener(OnModeChangeListener omcl){
		this.omcls.add(omcl);
	}
	private void notifyOnModeChangeListeners(){
		for (int i = 0; i < omcls.size(); i++) {
			omcls.get(i).modeChanged(this.mode);
		}
	}

	public void setMode(int mode){
		this.mode=mode;
		notifyOnModeChangeListeners();
	}

	@Deprecated
	public int getMode(){
		return mode;
	}

	public boolean higherThanOET(int mode){
		if (this.mode>=mode) return true;
		else return false;
	}
	
}
