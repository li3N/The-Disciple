package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.DelayedGainStrengthPower;


public class Flay extends MetricsCard {
	public static final String ID = "Flay";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int ATTACK_DMG = 4;
	private static final int UPGRADE_PLUS_DMG = 1;
	private static final int TURNS_TO_SHRINK = 1;
	private static final int SHRINK_UPGRADE = 1;

	public Flay() {
		super(ID, NAME, "images/cards/Flay.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ALL_ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = TURNS_TO_SHRINK;
		this.magicNumber = this.baseMagicNumber;
		this.isMultiDamage = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int block = m.currentBlock;

	    AbstractDungeon.actionManager.addToBottom(
	    		new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));

	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (mo.currentBlock < this.damage) {
	 		    AbstractDungeon.actionManager.addToBottom(
	 		    	new ApplyPowerAction(mo, p, new StrengthPower(mo, -(this.damage-mo.currentBlock)), -(this.damage-mo.currentBlock), true, AbstractGameAction.AttackEffect.NONE));

	 		    if (this.upgraded) {
		 		    AbstractDungeon.actionManager.addToBottom(
		 		    	new ApplyPowerAction(mo, p, new DelayedGainStrengthPower(mo, this.magicNumber, this.damage-mo.currentBlock), this.damage-mo.currentBlock, true, AbstractGameAction.AttackEffect.NONE));
		 		} else {
		 			AbstractDungeon.actionManager.addToBottom(
	          			new ApplyPowerAction(mo, p, new GainStrengthPower(mo, this.damage-mo.currentBlock), this.damage-mo.currentBlock, true, AbstractGameAction.AttackEffect.NONE));
		 		}
			}
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Flay();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			// upgradeDamage(UPGRADE_PLUS_DMG);
			upgradeMagicNumber(SHRINK_UPGRADE);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}