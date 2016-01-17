package animation;
import java.io.Serializable;
import java.util.*;


public class InterpolatableProperty implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	double currentVal, defaultVal;

	//private ArrayList<Keyframe> keyframes=new ArrayList<>();
	transient int i;
    private KeyframedValueCurve curve;


	public InterpolatableProperty(double v){
		this.defaultVal=v;
		this.currentVal=v;
        curve=new KeyframedValueCurve();
	}
	public InterpolatableProperty(InterpolatableProperty p){
		this.currentVal = p.currentVal;
		this.defaultVal=p.defaultVal;
		this.curve=new KeyframedValueCurve(p.curve);
	}

	/* @Deprecated
		You shouldn't take the array like this.
	 */
	@Deprecated
	public List<Keyframe> getKeyframes() {
		return curve.getKeyframes();
	}
	public void addKeyframe(Keyframe kf){
		curve.getKeyframes().add(kf);
		sortKeyframesByTime();
        recalculateCurve();
	}
	public void addKeyframe(double time){
		addKeyframe(new Keyframe(time,currentVal));
		sortKeyframesByTime();
		recalculateCurve();
	}
    public void recalculateCurve(){
        curve.recalculate();
    }
	private void sortKeyframesByTime(){
		Collections.sort(curve.getKeyframes());
	}
    public Keyframe getKeyframe(int i){
        return curve.getKeyframes().get(i);
    }
    public int getNumKeyframes(){
        return curve.getKeyframes().size();
    }

	public void setTime(double time){
		currentVal=getValueAtTime(time);
	}

	public double getValueAtTime(double time){
		return curve.getValueAtTime(time,currentVal);
	}
	
	public void setValue(double v){
		this.currentVal=v;
	}
	public double getValue(){
		return this.currentVal;
	}

	public void clear(){
		this.curve.getKeyframes().clear();
	}

    public KeyframedValueCurve getKeyframeValueCurve(){
        return this.curve;
    }

	public void clearKeyframes(){
		this.curve.getKeyframes().clear();
		recalculateCurve();
	}
	public void deleteNear(double time){
		Keyframe target=null;
		double minTime=100000;

		for (int j = 0; j < curve.getKeyframes().size(); j++) {
			if(Math.abs(time-curve.getKeyframes().get(j).getTime())<minTime){
				minTime=Math.abs(time-curve.getKeyframes().get(j).getTime());
				target=curve.getKeyframes().get(j);
			}
		}
		if (target==null){
			System.out.println("Keyframe Deletion Failed.");
		}else{
			curve.getKeyframes().remove(target);
			recalculateCurve();
		}
	}
}
