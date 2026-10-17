package simu.model;

import simu.framework.ITapahtumanTyyppi;

/**
 * Sisältää autopesulasimulaatiossa käytettävät tapahtumatyypit.
 * ARR1 tarkottaa kun uusi asiakas saapuu simulaatioon.
 * Muut tarkottaa milloin asiakas on valmis siinä kyseisessä palvelupisteessä
 * ja se on valmis siirtymään seuraavaan vaiheeseen.
 */
public enum TapahtumanTyyppi implements ITapahtumanTyyppi{
	/**
	 * Asiakas saapuu simulaatioon.
	 */
	ARR1,
	/**
	 * Palvelupisteen palvelu valmistuu.
	 */
	PALVELUPISTE_VALMIS,
	/**
	 * Basic pesu valmistuu.
	 */
	BASIC_VALMIS,
	/**
	 * Premium pesu valmistuu.
	 */
	PREMIUM_VALMIS,
	/**
	 * Vahaus valmistuu.
	 */
	VAHAUS_VALMIS,
	/**
	 * Kuivaus valmistuu.
	 */
	KUIVAUS_VALMIS;

}
