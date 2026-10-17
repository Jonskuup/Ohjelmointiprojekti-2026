package simu.model;

import simu.framework.ITapahtumanTyyppi;

/**
 * Sisältää autopesulasimulaatiossa käytettävät tapahtumatyypit.
 * ARR1 tarkottaa kun uusi asiakas saapuu simulaatioon.
 * Muut tarkottaa milloin asiakas on valmis siinä kyseisessä palvelupisteessä
 * ja se on valmis siirtymään seuraavaan vaiheeseen.
 */
public enum TapahtumanTyyppi implements ITapahtumanTyyppi{
	ARR1, PALVELUPISTE_VALMIS, BASIC_VALMIS, PREMIUM_VALMIS, VAHAUS_VALMIS, KUIVAUS_VALMIS;

}
