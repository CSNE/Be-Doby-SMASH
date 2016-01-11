package managers;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import interfaces.OnObjectSelectedListener;
import interfaces.OnObjectUpdateListener;
import interfaces.OnTimeUpdateListener;
import shapes.TransformableShape;

public class ObjectsManager implements OnTimeUpdateListener,Serializable{

    private static transient final String[] descriptors = {"abandoned", "able", "absolute", "adorable", "adventurous", "academic", "acceptable", "acclaimed", "accomplished", "accurate", "aching", "acidic", "acrobatic", "active", "actual", "adept", "admirable", "admired", "adolescent", "adorable", "adored", "advanced", "afraid", "affectionate", "aged", "aggravating", "aggressive", "agile", "agitated", "agonizing", "agreeable", "ajar", "alarmed", "alarming", "alert", "alienated", "alive", "all", "altruistic", "amazing", "ambitious", "ample", "amused", "amusing", "anchored", "ancient", "angelic", "angry", "anguished", "animated", "annual", "another", "antique", "anxious", "any", "apprehensive", "appropriate", "apt", "arctic", "arid", "aromatic", "artistic", "ashamed", "assured", "astonishing", "athletic", "attached", "attentive", "attractive", "austere", "authentic", "authorized", "automatic", "avaricious", "average", "aware", "awesome", "awful", "awkward", "babyish", "bad", "back", "baggy", "bare", "barren", "basic", "beautiful", "belated", "beloved", "beneficial", "better", "best", "bewitched", "big", "big-hearted", "biodegradable", "bite-sized", "bitter", "black", "black-and-white", "bland", "blank", "blaring", "bleak", "blind", "blissful", "blond", "blue", "blushing", "bogus", "boiling", "bold", "bony", "boring", "bossy", "both", "bouncy", "bountiful", "bowed", "brave", "breakable", "brief", "bright", "brilliant", "brisk", "broken", "bronze", "brown", "bruised", "bubbly", "bulky", "bumpy", "buoyant", "burdensome", "burly", "bustling", "busy", "buttery", "buzzing", "calculating", "calm", "candid", "canine", "capital", "carefree", "careful", "careless", "caring", "cautious", "cavernous", "celebrated", "charming", "cheap", "cheerful", "cheery", "chief", "chilly", "chubby", "circular", "classic", "clean", "clear", "clear-cut", "clever", "close", "closed", "cloudy", "clueless", "clumsy", "cluttered", "coarse", "cold", "colorful", "colorless", "colossal", "comfortable", "common", "compassionate", "competent", "complete", "complex", "complicated", "composed", "concerned", "concrete", "confused", "conscious", "considerate", "constant", "content", "conventional", "cooked", "cool", "cooperative", "coordinated", "corny", "corrupt", "costly", "courageous", "courteous", "crafty", "crazy", "creamy", "creative", "creepy", "criminal", "crisp", "critical", "crooked", "crowded", "cruel", "crushing", "cuddly", "cultivated", "cultured", "cumbersome", "curly", "curvy", "cute", "cylindrical", "damaged", "damp", "dangerous", "dapper", "daring", "darling", "dark", "dazzling", "dead", "deadly", "deafening", "dear", "dearest", "decent", "decimal", "decisive", "deep", "defenseless", "defensive", "defiant", "deficient", "definite", "definitive", "delayed", "delectable", "delicious", "delightful", "delirious", "demanding", "dense", "dental", "dependable", "dependent", "descriptive", "deserted", "detailed", "determined", "devoted", "different", "difficult", "digital", "diligent", "dim", "dimpled", "dimwitted", "direct", "disastrous", "discrete", "disfigured", "disgusting", "disloyal", "dismal", "distant", "downright", "dreary", "dirty", "disguised", "dishonest", "dismal", "distant", "distinct", "distorted", "dizzy", "dopey", "doting", "double", "downright", "drab", "drafty", "dramatic", "dreary", "droopy", "dry", "dual", "dull", "dutiful", "each", "eager", "earnest", "early", "easy", "easy-going", "ecstatic", "edible", "educated", "elaborate", "elastic", "elated", "elderly", "electric", "elegant", "elementary", "elliptical", "embarrassed", "embellished", "eminent", "emotional", "empty", "enchanted", "enchanting", "energetic", "enlightened", "enormous", "enraged", "entire", "envious", "equal", "equatorial", "essential", "esteemed", "ethical", "euphoric", "even", "evergreen", "everlasting", "every", "evil", "exalted", "excellent", "exemplary", "exhausted", "excitable", "excited", "exciting", "exotic", "expensive", "experienced", "expert", "extraneous", "extroverted", "extra-large", "extra-small", "fabulous", "failing", "faint", "fair", "faithful", "fake", "false", "familiar", "famous", "fancy", "fantastic", "far", "faraway", "far-flung", "far-off", "fast", "fat", "fatal", "fatherly", "favorable", "favorite", "fearful", "fearless", "feisty", "feline", "female", "feminine", "few", "fickle", "filthy", "fine", "finished", "firm", "first", "firsthand", "fitting", "fixed", "flaky", "flamboyant", "flashy", "flat", "flawed", "flawless", "flickering", "flimsy", "flippant", "flowery", "fluffy", "fluid", "flustered", "focused", "fond", "foolhardy", "foolish", "forceful", "forked", "formal", "forsaken", "forthright", "fortunate", "fragrant", "frail", "frank", "frayed", "free", "French", "fresh", "frequent", "friendly", "frightened", "frightening", "frigid", "frilly", "frizzy", "frivolous", "front", "frosty", "frozen", "frugal", "fruitful", "full", "fumbling", "functional", "funny", "fussy", "fuzzy", "gargantuan", "gaseous", "general", "generous", "gentle", "genuine", "giant", "giddy", "gigantic", "gifted", "giving", "glamorous", "glaring", "glass", "gleaming", "gleeful", "glistening", "glittering", "gloomy", "glorious", "glossy", "glum", "golden", "good", "good-natured", "gorgeous", "graceful", "gracious", "grand", "grandiose", "granular", "grateful", "grave", "gray", "great", "greedy", "green", "gregarious", "grim", "grimy", "gripping", "grizzled", "gross", "grotesque", "grouchy", "grounded", "growing", "growling", "grown", "grubby", "gruesome", "grumpy", "guilty", "gullible", "gummy", "hairy", "half", "handmade", "handsome", "handy", "happy", "happy-go-lucky", "hard", "hard-to-find", "harmful", "harmless", "harmonious", "harsh", "hasty", "hateful", "haunting", "healthy", "heartfelt", "hearty", "heavenly", "heavy", "hefty", "helpful", "helpless", "hidden", "hideous", "high", "high-level", "hilarious", "hoarse", "hollow", "homely", "honest", "honorable", "honored", "hopeful", "horrible", "hospitable", "hot", "huge", "humble", "humiliating", "humming", "humongous", "hungry", "hurtful", "husky", "icky", "icy", "ideal", "idealistic", "identical", "idle", "idiotic", "idolized", "ignorant", "ill", "illegal", "ill-fated", "ill-informed", "illiterate", "illustrious", "imaginary", "imaginative", "immaculate", "immaterial", "immediate", "immense", "impassioned", "impeccable", "impartial", "imperfect", "imperturbable", "impish", "impolite", "important", "impossible", "impractical", "impressionable", "impressive", "improbable", "impure", "inborn", "incomparable", "incompatible", "incomplete", "inconsequential", "incredible", "indelible", "inexperienced", "indolent", "infamous", "infantile", "infatuated", "inferior", "infinite", "informal", "innocent", "insecure", "insidious", "insignificant", "insistent", "instructive", "insubstantial", "intelligent", "intent", "intentional", "interesting", "internal", "international", "intrepid", "ironclad", "irresponsible", "irritating", "itchy", "jaded", "jagged", "jam-packed", "jaunty", "jealous", "jittery", "joint", "jolly", "jovial", "joyful", "joyous", "jubilant", "judicious", "juicy", "jumbo", "junior", "jumpy", "juvenile", "kaleidoscopic", "keen", "key", "kind", "kindhearted", "kindly", "klutzy", "knobby", "knotty", "knowledgeable", "knowing", "known", "kooky", "kosher", "lame", "lanky", "large", "last", "lasting", "late", "lavish", "lawful", "lazy", "leading", "lean", "leafy", "left", "legal", "legitimate", "light", "lighthearted", "likable", "likely", "limited", "limp", "limping", "linear", "lined", "liquid", "little", "live", "lively", "livid", "loathsome", "lone", "lonely", "long", "long-term", "loose", "lopsided", "lost", "loud", "lovable", "lovely", "loving", "low", "loyal", "lucky", "lumbering", "luminous", "lumpy", "lustrous", "luxurious", "mad", "made-up", "magnificent", "majestic", "major", "male", "mammoth", "married", "marvelous", "masculine", "massive", "mature", "meager", "mealy", "mean", "measly", "meaty", "medical", "mediocre", "medium", "meek", "mellow", "melodic", "memorable", "menacing", "merry", "messy", "metallic", "mild", "milky", "mindless", "miniature", "minor", "minty", "miserable", "miserly", "misguided", "misty", "mixed", "modern", "modest", "moist", "monstrous", "monthly", "monumental", "moral", "mortified", "motherly", "motionless", "mountainous", "muddy", "muffled", "multicolored", "mundane", "murky", "mushy", "musty", "muted", "mysterious", "naive", "narrow", "nasty", "natural", "naughty", "nautical", "near", "neat", "necessary", "needy", "negative", "neglected", "negligible", "neighboring", "nervous", "new", "next", "nice", "nifty", "nimble", "nippy", "nocturnal", "noisy", "nonstop", "normal", "notable", "noted", "noteworthy", "novel", "noxious", "numb", "nutritious", "nutty", "obedient", "obese", "oblong", "oily", "oblong", "obvious", "occasional", "odd", "oddball", "offbeat", "offensive", "official", "old", "old-fashioned", "only", "open", "optimal", "optimistic", "opulent", "orange", "orderly", "organic", "ornate", "ornery", "ordinary", "original", "other", "our", "outlying", "outgoing", "outlandish", "outrageous", "outstanding", "oval", "overcooked", "overdue", "overjoyed", "overlooked", "palatable", "pale", "paltry", "parallel", "parched", "partial", "passionate", "past", "pastel", "peaceful", "peppery", "perfect", "perfumed", "periodic", "perky", "personal", "pertinent", "pesky", "pessimistic", "petty", "phony", "physical", "piercing", "pink", "pitiful", "plain", "plaintive", "plastic", "playful", "pleasant", "pleased", "pleasing", "plump", "plush", "polished", "polite", "political", "pointed", "pointless", "poised", "poor", "popular", "portly", "posh", "positive", "possible", "potable", "powerful", "powerless", "practical", "precious", "present", "prestigious", "pretty", "precious", "previous", "pricey", "prickly", "primary", "prime", "pristine", "private", "prize", "probable", "productive", "profitable", "profuse", "proper", "proud", "prudent", "punctual", "pungent", "puny", "pure", "purple", "pushy", "putrid", "puzzled", "puzzling", "quaint", "qualified", "quarrelsome", "quarterly", "queasy", "querulous", "questionable", "quick", "quick-witted", "quiet", "quintessential", "quirky", "quixotic", "quizzical", "radiant", "ragged", "rapid", "rare", "rash", "raw", "recent", "reckless", "rectangular", "ready", "real", "realistic", "reasonable", "red", "reflecting", "regal", "regular", "reliable", "relieved", "remarkable", "remorseful", "remote", "repentant", "required", "respectful", "responsible", "repulsive", "revolving", "rewarding", "rich", "rigid", "right", "ringed", "ripe", "roasted", "robust", "rosy", "rotating", "rotten", "rough", "round", "rowdy", "royal", "rubbery", "rundown", "ruddy", "rude", "runny", "rural", "rusty", "sad", "safe", "salty", "same", "sandy", "sane", "sarcastic", "sardonic", "satisfied", "scaly", "scarce", "scared", "scary", "scented", "scholarly", "scientific", "scornful", "scratchy", "scrawny", "second", "secondary", "second-hand", "secret", "self-assured", "self-reliant", "selfish", "sentimental", "separate", "serene", "serious", "serpentine", "several", "severe", "shabby", "shadowy", "shady", "shallow", "shameful", "shameless", "sharp", "shimmering", "shiny", "shocked", "shocking", "shoddy", "short", "short-term", "showy", "shrill", "shy", "sick", "silent", "silky", "silly", "silver", "similar", "simple", "simplistic", "sinful", "single", "sizzling", "skeletal", "skinny", "sleepy", "slight", "slim", "slimy", "slippery", "slow", "slushy", "small", "smart", "smoggy", "smooth", "smug", "snappy", "snarling", "sneaky", "sniveling", "snoopy", "sociable", "soft", "soggy", "solid", "somber", "some", "spherical", "sophisticated", "sore", "sorrowful", "soulful", "soupy", "sour", "Spanish", "sparkling", "sparse", "specific", "spectacular", "speedy", "spicy", "spiffy", "spirited", "spiteful", "splendid", "spotless", "spotted", "spry", "square", "squeaky", "squiggly", "stable", "staid", "stained", "stale", "standard", "starchy", "stark", "starry", "steep", "sticky", "stiff", "stimulating", "stingy", "stormy", "straight", "strange", "steel", "strict", "strident", "striking", "striped", "strong", "studious", "stunning", "stupendous", "stupid", "sturdy", "stylish", "subdued", "submissive", "substantial", "subtle", "suburban", "sudden", "sugary", "sunny", "super", "superb", "superficial", "superior", "supportive", "sure-footed", "surprised", "suspicious", "svelte", "sweaty", "sweet", "sweltering", "swift", "sympathetic", "tall", "talkative", "tame", "tan", "tangible", "tart", "tasty", "tattered", "taut", "tedious", "teeming", "tempting", "tender", "tense", "tepid", "terrible", "terrific", "testy", "thankful", "that", "these", "thick", "thin", "third", "thirsty", "this", "thorough", "thorny", "those", "thoughtful", "threadbare", "thrifty", "thunderous", "tidy", "tight", "timely", "tinted", "tiny", "tired", "torn", "total", "tough", "traumatic", "treasured", "tremendous", "tragic", "trained", "tremendous", "triangular", "tricky", "trifling", "trim", "trivial", "troubled", "true", "trusting", "trustworthy", "trusty", "truthful", "tubby", "turbulent", "twin", "ugly", "ultimate", "unacceptable", "unaware", "uncomfortable", "uncommon", "unconscious", "understated", "unequaled", "uneven", "unfinished", "unfit", "unfolded", "unfortunate", "unhappy", "unhealthy", "uniform", "unimportant", "unique", "united", "unkempt", "unknown", "unlawful", "unlined", "unlucky", "unnatural", "unpleasant", "unrealistic", "unripe", "unruly", "unselfish", "unsightly", "unsteady", "unsung", "untidy", "untimely", "untried", "untrue", "unused", "unusual", "unwelcome", "unwieldy", "unwilling", "unwitting", "unwritten", "upbeat", "upright", "upset", "urban", "usable", "used", "useful", "useless", "utilized", "utter", "vacant", "vague", "vain", "valid", "valuable", "vapid", "variable", "vast", "velvety", "venerated", "vengeful", "verifiable", "vibrant", "vicious", "victorious", "vigilant", "vigorous", "villainous", "violet", "violent", "virtual", "virtuous", "visible", "vital", "vivacious", "vivid", "voluminous", "wan", "warlike", "warm", "warmhearted", "warped", "wary", "wasteful", "watchful", "waterlogged", "watery", "wavy", "wealthy", "weak", "weary", "webbed", "wee", "weekly", "weepy", "weighty", "weird", "welcome", "well-documented", "well-groomed", "well-informed", "well-lit", "well-made", "well-off", "well-to-do", "well-worn", "wet", "which", "whimsical", "whirlwind", "whispered", "white", "whole", "whopping", "wicked", "wide", "wide-eyed", "wiggly", "wild", "willing", "wilted", "winding", "windy", "winged", "wiry", "wise", "witty", "wobbly", "woeful", "wonderful", "wooden", "woozy", "wordy", "worldly", "worn", "worried", "worrisome", "worse", "worst", "worthless", "worthwhile", "worthy", "wrathful", "wretched", "writhing", "wrong", "wry", "yawning", "yearly", "yellow", "yellowish", "young", "youthful", "yummy", "zany", "zealous", "zesty", "zigzag"};

    public static transient final int NORMAL_EDITING = 124;
    public static transient final int FREE_FORM_EDITING = 6432;
    public static transient final int VERTEX_EDITING = 6123;
    public static transient final int NOTHING_SELECTED = 5312;
    private transient int mode = NOTHING_SELECTED;

    public int getMode() {
        return this.mode;
    }

    static ObjectsManager inst = new ObjectsManager();

    public synchronized static ObjectsManager getInstance() {
        return inst;
    }

    private ObjectsManager() {

    }

    private ArrayList<TransformableShape> shapes = new ArrayList<>();
    private Color canvasColor = Color.DARK_GRAY;
    private transient ArrayList<OnObjectSelectedListener> oosls = new ArrayList<>();
    private transient ArrayList<OnObjectUpdateListener> oouls = new ArrayList<>();

    private transient TimeManager tm = TimeManager.getInstance();


    private transient TransformableShape iveTarget;
    private transient TransformableShape ffTarget;

    private void copy(ObjectsManager om) {
        this.shapes = om.shapes;
        this.canvasColor = om.canvasColor;
    }


    public void addOnObjectSelectedListener(OnObjectSelectedListener oosl) {
        this.oosls.add(oosl);
    }

    public void notifyOnObjectSelectedListeners(TransformableShape shape) {
        for (OnObjectSelectedListener oosl : oosls) oosl.onSelect(shape);
    }

    public void addOnObjectUpdateListener(OnObjectUpdateListener ooul) {
        this.oouls.add(ooul);
    }

    public void notifyOnObjectUpdateListeners() {
        for (OnObjectUpdateListener ooul : oouls) ooul.updateObject();
    }


    public int getNumOfObjects() {
        return shapes.size();
    }

    public TransformableShape getObject(int i) {
        return shapes.get(i);
    }

    public TransformableShape findObjectWithName(String name) {
        for (int i = 0; i < shapes.size(); i++) {
            if (name.equals(shapes.get(i).getName())) return shapes.get(i);
        }
        return null;
    }


    public boolean selectObject(double x, double y) {
        int maxPriority = 0;
        for (TransformableShape object : shapes) {
            object.deselect();
            if (object.isInside(x, y)) {
                object.incrementSelectionPriority();
                if (object.getSelectionPriority() > maxPriority) maxPriority = object.getSelectionPriority();
            }
        }
        for (TransformableShape object : shapes) {
            if (object.isInside(x, y)) {
                if (object.getSelectionPriority() == maxPriority) {
                    object.select();
                    object.resetSelectionPriority();
                    notifyOnObjectSelectedListeners(object);
                    mode = NORMAL_EDITING;
                    return true;
                }
            }
        }
        notifyOnObjectSelectedListeners(null);
        mode = NOTHING_SELECTED;
        return false;
    }

    public void selectObject(TransformableShape shape) {
        if (shape==null){
            deselectAll();
            return;
        }
        for (TransformableShape s : shapes) s.deselect();
        shape.select();
        notifyOnObjectSelectedListeners(shape);
        mode = NORMAL_EDITING;

    }
    public void deselectAll(){
        for (TransformableShape s : shapes) s.deselect();
        notifyOnObjectSelectedListeners(null);
        mode = NOTHING_SELECTED;
    }

    public TransformableShape activeObject() {
        for (TransformableShape object : shapes) {
            if (object.isSelected()) return object;
        }
        return null;
    }

    public void deleteObject(TransformableShape thing) {
        shapes.remove(thing);
    }

    public void updateObjectsToTime(double time) {
        for (TransformableShape s : shapes) {
            s.setValueFromTime(time);
        }
        notifyOnObjectUpdateListeners();
    }

    public void enterIVE(TransformableShape target) {
        mode = VERTEX_EDITING;
        iveTarget = target;
    }

    public void exitIVE() {
        mode = NORMAL_EDITING;
        iveTarget = null;
    }

    public TransformableShape getIveTarget() {
        return this.iveTarget;
    }


    public void enterFreeForm() {
        mode = FREE_FORM_EDITING;
        ffTarget = createShape();
    }

    public boolean exitFreeForm() {
        mode = NOTHING_SELECTED;
        if (ffTarget.getNumPoints()<3) {
            shapes.remove(ffTarget);
            ffTarget = null;
            return false;
        }else {
            ffTarget.centerVertices();
            ffTarget.select();
            notifyOnObjectSelectedListeners(ffTarget);
            mode=NORMAL_EDITING;
            ffTarget = null;
            return true;
        }

    }

    public TransformableShape getFfTarget() {
        return this.ffTarget;
    }


    public TransformableShape createShape() {
        Random rand = new Random();
        TransformableShape newShape = new TransformableShape();
        String desc = descriptors[rand.nextInt(descriptors.length)];
        if (desc.charAt(0) == 'a' || desc.charAt(0) == 'e' || desc.charAt(0) == 'i' || desc.charAt(0) == 'o' || desc.charAt(0) == 'u')
            newShape.setName("An " + desc + " shape");
        else newShape.setName("A " + desc + " shape");
        shapes.add(newShape);
        return newShape;
    }

    public TransformableShape duplicateShape(TransformableShape shape) {
        TransformableShape newShape = new TransformableShape(shape);
        newShape.setName(shape.getName() + "_Copy");
        selectObject(newShape);
        shapes.add(newShape);
        return newShape;
    }

    public void saveShapesToFile(File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized data saved");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadShapesFromFile(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ObjectsManager newOM = (ObjectsManager) in.readObject();
            this.copy(newOM);
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clearShapes() {
        shapes.clear();
    }

    public void setCanvasColor(Color c) {
        this.canvasColor = c;
    }
    public Color getCanvasColor(){
        return this.canvasColor;
    }

    @Override
    public void updateTime() {
        updateObjectsToTime(tm.getCurrentAnimationTime());
    }
}
