package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import org.bukkit.Material;

import java.util.List;

public class EXGEntrySettings {


	// -------------------------------------------------- //


	private final EXGEntryType entryType;
	private String entryDisplayName;
	private List<EXGEntryType> acceptedTypes;

	// For String
	int minLength = -1;
	int maxLength = -1;
	String charactersListPath;
	boolean mustBeNumber = false;
	String equalsToSomething;

	// For Material
	private Material[] acceptedMaterials;
	private String materialsListPath;


	// -------------------------------------------------- //


	public EXGEntrySettings(EXGEntryType entryType) {
		this.entryType = entryType;
	}


	// -------------------------------------------------- //


	// General
	public EXGEntryType getType() {
		return entryType;
	}
	public String getEntryDisplayName() {
		return entryDisplayName;
	}
	public List<EXGEntryType> getAcceptedTypes() {
		return acceptedTypes;
	}

	// For String
	public int getMinLength() {
		return minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public String getCharactersListPath() {
		return charactersListPath;
	}
	public boolean isMustBeNumber() {
		return mustBeNumber;
	}
	public String getEqualsToSomething() {
		return equalsToSomething;
	}

	// For Material
	public String getMaterialsListPath() {
		return materialsListPath;
	}


	// -------------------------------------------------- //


	// General
	public EXGEntrySettings setEntryDisplayName(String entryDisplayName) {
		this.entryDisplayName = entryDisplayName;
		return this;
	}
	public EXGEntrySettings setAcceptedTypes(List<EXGEntryType> acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
		return this;
	}


	// For String
	public EXGEntrySettings setMinLength(int minLength) {
		this.minLength = minLength;
		return this;
	}
	public EXGEntrySettings setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}
	public EXGEntrySettings setCharactersListPath(String charactersListPath) {
		this.charactersListPath = charactersListPath;
		return this;
	}
	public EXGEntrySettings setMustBeNumber(boolean mustBeNumber) {
		this.mustBeNumber = mustBeNumber;
		return this;
	}
	public EXGEntrySettings setEqualsToSomething(String equalsToSomething) {
		this.equalsToSomething = equalsToSomething;
		return this;
	}


	// For Material
	public EXGEntrySettings setMaterialsListPath(String materialsListPath) {
		this.materialsListPath = materialsListPath;
		return this;
	}


	// -------------------------------------------------- //
}
