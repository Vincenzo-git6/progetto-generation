package org.example.dandd.model.entities.enums;

public enum CharacterType
{
	CODECLEANER("Code Cleaner", "pulisce"),
	CODETHIEF("",""),
	DATAMYSTIC("",""),
	GITBARD("",""),
	INFRASTRUCTURE("",""),
	TROUBLESHOOTER("",""),
	OMNICODER("","");

	private final String displayName;
	private final String description;

	CharacterType(String displayName, String description) {
		this.displayName = displayName;
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}
}
