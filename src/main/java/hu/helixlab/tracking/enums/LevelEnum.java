package hu.helixlab.tracking.enums;

public enum LevelEnum {
	veryEasy ("Nagyon könnyű"), easy ("Könnyű"), medium ("Közepes"), hard ("Nehéz"), veryHard ("Nagyon nehéz") ;
	
	public String hunName;

	private LevelEnum(String hunName) {
		this.hunName = hunName;
	}

	public String getHunName () {
		return this.hunName;
	}

	public static LevelEnum getLevelByNumber (Integer level){
		switch (level) {
			case 1:
				return LevelEnum.veryEasy;
			case 2:
				return LevelEnum.easy;
			case 3:
				return LevelEnum.medium;
			case 4:
				return LevelEnum.hard;
			case 5:
				return LevelEnum.veryHard;
			default:
				return null;
		}
	}
}
