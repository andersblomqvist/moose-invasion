package com.andblomqdasberg.mooseinvasion.weapon;

import com.andblomqdasberg.mooseinvasion.GameManager;
import com.andblomqdasberg.mooseinvasion.audio.AudioPlayer;
import com.andblomqdasberg.mooseinvasion.entity.Projectile;
import com.andblomqdasberg.mooseinvasion.particle.ParticleType;

/**
 * 	Class for handling weapons, easier if we want to add
 * 	more than just 1 weapon
 * 
 * 	@author Anders Blomqvist
 * 	@author David Åsberg
 */
public class Weapon {

	// Weapon stats
    private float fireRate;
    private int magazineCapacity, bulletsInMag, reloadTime, ticksSpentReloading, ticksSinceLastShot, damage;
    private boolean reloading, fmj;

    AudioPlayer audioPlayer = null;

    // Upgrade trackers
    public int damageBought = 1;
    public int fireRateBought = 1;
    public int reloadBought = 1;
    public int ammoBought = 1;

    /**
     * 	Default constructor. Creates a new weapon with specified stats
     */
    public Weapon(float fireRate, int damage, int magazineCapacity, int reloadTime) {
        this.fireRate = fireRate;
        this.damage = damage;
        this.magazineCapacity = magazineCapacity;
        this.bulletsInMag = magazineCapacity;
        this.reloadTime = reloadTime;
        reloading = false;
    }
    
    /**
     *	Update for reloading and fire rate timings
     */
    public void tick() {
        if (reloading) {
            ticksSpentReloading++;
            if(ticksSpentReloading == reloadTime) {
                bulletsInMag = magazineCapacity;
                reloading = false;
                playEndReload();
            }
        }
        
        else if(!isFireReady())
            ticksSinceLastShot++;
    }

    /**
     * 	@returns true/false wheter if we are ready to shoot or not
     */
    public boolean isFireReady(){
        return (bulletsInMag > 0 && !reloading && ticksSinceLastShot > 60/fireRate);
    }

    /**
     * 	Fire method. Spawns a projectile and graphics
     * 
     * 	@param x offset to gun barrel end
     * 	@param y offset to gun barrel end
     */
    public void fire(float x, float y) {
    	
    	// Leave if we are not ready to fire yet
        if (!isFireReady())
            return;
        
        GameManager.sInstance.spawnParticles(ParticleType.AMMO, 1, x, y);
        GameManager.sInstance.spawnEntity(new Projectile(x, y, damage, fmj));
        bulletsInMag--;
        ticksSinceLastShot = 0;

        playGunFire();

        // Automatic reload
        if(bulletsInMag == 0) {
            reload();
        }
    }

    /**
     * 	Reload weapon if mag is not full
     */
    public void reload() {
        if(reloading || bulletsInMag == magazineCapacity)
            return;
        ticksSpentReloading = 0;
        reloading = true;
        playStartReload();
    }

    /**
     * 	Upgrade weapon
     * 
     * 	@param type what type of upgrade we bought
     */
    public void upgrade(WeaponUpgrade type) {
    	switch(type) {
			case DAMAGE:
				// If we already one shot the moose we add FMJ instead.
				if(damage > 100) {
					fmj = true;
					return;
				}
				int damageIncrease = (int) (((double) getDamage())*1.25);
				System.out.println("new: " + damageIncrease);
				damageBought++;
				setDamage(damageIncrease);
				break;
			case AMMO:
				int ammoCapIncrease = (int) (((double) getMagazineCapacity())*1.25);
				ammoBought++;
				setMagazineCapacity(ammoCapIncrease);
				break;
			case RELOAD:
				reloadBought++;
				int reloadTimeDecrease = (int) (((double) getReloadTime())*0.87);
				setReloadTime(reloadTimeDecrease);
				break;
			case FIRERATE:
				fireRateBought++;
				int fireRateIncrease = (int) (((double) getFireRate())*1.1);
				setFireRate(fireRateIncrease);
				break;
		}
    }
    
    /**
     * 	@param what type of upgrade we want cost for
     * 
     * 	@returns the gold cost for specific upgrade type
     */
    public int getUpgradeCost(WeaponUpgrade type) {
    	switch(type) {
	    	case DAMAGE:
	    		if(fmj)
	    			return upgradeFormula(-1);
	    		else
	    			return upgradeFormula(damageBought);
	    	case AMMO:
	    		return upgradeFormula(ammoBought);
	    	case RELOAD:
	    		return upgradeFormula(reloadBought);
	    	case FIRERATE:
	    		return upgradeFormula(fireRateBought);
			default:
				return 999;
    	}
    }
    
    /**
     * 	Formula for increasing cost of weapon upgrade:
     * 		f(x) = 1 + x(6x - x).
     * 
     *  Each time the upgrade is bought the cost rises.
     * 	
     * 	@param variable how many times we've bought the upgrade.
     * 		   -1 as argument indicates that the weapon is fully
     * 		   upgraded.
     * 
     * 	@return the cost
     */
    private int upgradeFormula(int variable) {
    	if(variable != -1)
    		return 1 + variable*(6*variable - 2);
    	else
    		return -1;
    }
    
    /**
     * 	@category SOUNDS
     */
    
    private void playStartReload(){
        try {
            audioPlayer = new AudioPlayer("startreload.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
        audioPlayer.play();
    }

    private void playEndReload(){
        try {
            audioPlayer = new AudioPlayer("endreload.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
        audioPlayer.play();
    }

    private void playGunFire(){
        try {
            audioPlayer = new AudioPlayer("gunfire.wav");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        audioPlayer.play();
    }
    
    /**
     * 	@category GETTERS/SETTERS + help functions
     */
    
    public boolean isReloading(){
        return reloading;
    }

    public float reloadPercentage(){
        return (float)ticksSpentReloading / (float)reloadTime;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public int getMagazineCapacity() {
        return magazineCapacity;
    }

    public void setMagazineCapacity(int magazineCapacity) {
        this.magazineCapacity = magazineCapacity;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public int getBulletsInMag() {
    	return bulletsInMag;
    }
    
    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getDamage(){
        return damage;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }
}
