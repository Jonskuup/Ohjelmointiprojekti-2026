package simu.model;

import simu.framework.ITapahtumanTyyppi;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum TapahtumanTyyppi implements ITapahtumanTyyppi{
	ARR1, PALVELUPISTE_VALMIS, BASIC_VALMIS, PREMIUM_VALMIS, VAHAUS_VALMIS, KUIVAUS_VALMIS;

}
