package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.SlayLogRepositoryPort;
import com.frodo.glamdring.domain.models.Slay;
import com.frodo.glamdring.domain.models.TechTopic;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Outbound adapter for the Slay log — a hand-written, static list.
 * <p>
 * To publish a new slay: add a Slay.builder() entry below and redeploy.
 * There is deliberately no UI or database for this — entries are rare
 * enough that editing code is simpler than building a CMS for it.
 */
@Component
public class StaticSlayLogRepository implements SlayLogRepositoryPort {

    private static final List<Slay> SLAYS = List.of(

            Slay.builder()
                    .id("switch-statement-spell")
                    .title("The Spell That Needed a Rewrite")
                    .topic(TechTopic.SOFTWARE_ENGINEERING)
                    .smell("SpellCaster.cast(String type) was one giant switch: case \"fireball\", "
                            + "case \"frost\", case \"heal\". Every new spell meant opening the same "
                            + "method, adding a branch, and hoping the other branches still worked. "
                            + "The method had grown past two hundred lines, three people edited it "
                            + "every sprint, and each edit risked breaking a spell nobody was "
                            + "supposed to be touching.")
                    .theSlay("Introduced a Spell interface with a single cast(Caster, Target) method. "
                            + "Fireball, Frost and Heal each became their own class, and SpellCaster "
                            + "just asks the chosen Spell to cast itself. A new spell is a new class "
                            + "registered with the spellbook; SpellCaster is never opened again.")
                    .slayExample("""
                            public interface Spell {
                                String name();
                                void cast(Caster caster, Target target);
                            }

                            public class Fireball implements Spell {
                                public String name() { return "fireball"; }

                                public void cast(Caster caster, Target target) {
                                    caster.spendMana(30);
                                    target.takeDamage(caster.intellect() * 2);
                                    target.ignite();
                                }
                            }

                            public class Spellbook {
                                private final Map<String, Spell> spells;

                                public Spellbook(List<Spell> known) {
                                    this.spells = known.stream()
                                            .collect(toMap(Spell::name, Function.identity()));
                                }

                                public Spell find(String name) {
                                    return Optional.ofNullable(spells.get(name))
                                            .orElseThrow(() -> new UnknownSpellException(name));
                                }
                            }

                            public class SpellCaster {
                                private final Spellbook spellbook;

                                public SpellCaster(Spellbook spellbook) { this.spellbook = spellbook; }

                                public void cast(String name, Caster caster, Target target) {
                                    spellbook.find(name).cast(caster, target);
                                }
                            }
                            """)
                    .lesson("Open for extension, closed for modification: new behavior should arrive "
                            + "as new code, not as edits to code that already works. When adding a "
                            + "feature means reopening the same switch every time, that switch is "
                            + "begging to become a polymorphic seam.")
                    .smellExample("""
                            public class SpellCaster {

                                public void cast(String type, Caster caster, Target target) {
                                    switch (type) {
                                        case "fireball" -> {
                                            caster.spendMana(30);
                                            target.takeDamage(caster.intellect() * 2);
                                            target.ignite();
                                        }
                                        case "frost" -> {
                                            caster.spendMana(20);
                                            target.takeDamage(caster.intellect());
                                            target.slow(0.5);
                                        }
                                        case "heal" -> {
                                            caster.spendMana(25);
                                            target.restore(caster.intellect() * 3);
                                        }
                                        // ...200 more lines. A new spell? Edit this method.
                                        default -> throw new IllegalArgumentException(type);
                                    }
                                }
                            }
                            """)
                    .lessonExplanation("""
                            Open for extension means you can add new behavior. Closed for modification means you add it without editing code that already works and is already tested.

                            The trick is a seam: an interface that stands for the thing that varies. Here the Spell interface is the seam, and every spell is one implementation of it.

                            What this costs: an interface plus one small class per spell, and SpellCaster, which no longer changes. A Spellbook that maps names to spells is optional. SpellCaster can hold that map itself. It is only split out so lookup and casting each have one job.

                            What you get back: a new spell is a new class and nothing existing is opened, so you cannot break fireball while adding frost. Each spell is tested alone. Three people can add three spells without touching the same file.

                            When not to do it: with two or three spells that will never grow, the switch is simpler and perfectly fine. Add the seam when the same switch keeps being reopened, not before.
                            """)
                    .demo("A client wires up the spellbook and casts three spells. Then a brand new "
                            + "spell is added, and not one existing class is opened.",
                            """
                            // --- The client: a duel that needs to cast spells ---------------------
                            public class Duel {

                                private final SpellCaster spellCaster;

                                public Duel(SpellCaster spellCaster) {
                                    this.spellCaster = spellCaster;
                                }

                                public void play(Caster wizard, Target troll) {
                                    spellCaster.cast("frost", wizard, troll);     // slows the troll
                                    spellCaster.cast("fireball", wizard, troll);  // burns it
                                    spellCaster.cast("heal", wizard, wizard);     // patches up the wizard
                                }
                            }

                            // --- Composition root: the only place that knows every spell ----------
                            public class Game {

                                public static void main(String[] args) {
                                    Spellbook spellbook = new Spellbook(List.of(
                                            new Fireball(),
                                            new Frost(),
                                            new Heal()));

                                    Duel duel = new Duel(new SpellCaster(spellbook));
                                    duel.play(new Wizard("Gandalf", 40), new Troll(120));
                                }
                            }

                            // --- Later: the designer wants lightning. Add ONE class... ------------
                            public class LightningBolt implements Spell {

                                public String name() { return "lightning"; }

                                public void cast(Caster caster, Target target) {
                                    caster.spendMana(45);
                                    target.takeDamage(caster.intellect() * 3);
                                    target.stun(Duration.ofSeconds(2));
                                }
                            }

                            // --- ...and ONE line in the composition root. Nothing else changes. ---
                            Spellbook spellbook = new Spellbook(List.of(
                                    new Fireball(),
                                    new Frost(),
                                    new Heal(),
                                    new LightningBolt()));   // <- the only edit

                            // Duel, SpellCaster, Spellbook, Fireball, Frost and Heal: untouched,
                            // retested by nobody, broken by nothing.
                            spellCaster.cast("lightning", wizard, troll);

                            // --- Casting a spell nobody has learned fails loudly, not silently ----
                            spellCaster.cast("meteor", wizard, troll);
                            // -> UnknownSpellException: meteor
                            """)
                    .slainOn(2026, 10, 15)
                    .build(),

            Slay.builder()
                    .id("swiss-army-port")
                    .title("The Port That Did Everything")
                    .topic(TechTopic.SOFTWARE_ENGINEERING)
                    .smell("One CharacterPort interface with fourteen methods: loadCharacter(), "
                            + "saveCharacter(), deleteCharacter(), searchByGuild(), listLeaderboard(), "
                            + "exportToJson() and friends. The inventory screen only needed to load a "
                            + "character, yet its test double had to stub all fourteen. Adding a "
                            + "leaderboard filter meant touching an interface that the save-game code "
                            + "depended on, and recompiling everything downstream of it.")
                    .theSlay("Split the port along the lines of who uses it. LoadCharacterPort, "
                            + "SaveCharacterPort and LeaderboardQueryPort each carry only what one "
                            + "use case calls. The persistence adapter still implements all of them, "
                            + "but each application service now depends on just the one slice it "
                            + "needs, and its tests stub a single method.")
                    .lesson("No client should be forced to depend on methods it doesn't use. A fat "
                            + "interface is a promise made to every caller about things only some of "
                            + "them care about. Shape the interface to the consumer, not to the "
                            + "implementer, and one class is still free to implement many small ports.")
                    .slainOn(2026, 10, 15)
                    .build(),

            Slay.builder()
                    .id("blocking-reactor")
                    .title("The Blocking Reactor")
                    .topic(TechTopic.HEXAGONAL_ARCHITECTURE)
                    .smell("BlueskyAdapter used WebClient, Spring's async HTTP client — but called "
                            + ".block() on every request. All the weight of a reactive stack (Netty, "
                            + "native epoll transport, the works), none of the benefit. Just a slow, "
                            + "honest RestClient wearing a costume.")
                    .theSlay("Swapped to RestClient — same fluent API, but honest about being "
                            + "synchronous. Dropped spring-boot-starter-webflux entirely.")
                    .lesson("If you're calling .block(), you were never actually being reactive. "
                            + "Don't pay for machinery you don't use.")
                    .slainOn(2026, 9, 5)
                    .build(),

            Slay.builder()
                    .id("anemic-warrior")
                    .title("The Warrior Who Couldn't Fight")
                    .topic(TechTopic.DOMAIN_DRIVEN_DESIGN)
                    .smell("Warrior was a save file wearing a class name — getHealth(), getArmor(), "
                            + "getWeaponDamage(), all public, all mutable. CombatEngine did the actual "
                            + "fighting: rolled the hit, subtracted armor, called setHealth() on both "
                            + "sides, and checked if health had dropped to zero. The warrior never "
                            + "swung a sword; it just held numbers other code changed.")
                    .theSlay("Gave the fight back to the fighters. Warrior.attack(Warrior target) now "
                            + "resolves its own hit and calls target.takeDamage(amount); takeDamage() "
                            + "applies its own armor reduction and decides for itself whether it's "
                            + "now dead. CombatEngine still runs the turn order, but it can no longer "
                            + "reach into a warrior and move its health bar by hand.")
                    .lesson("Tell, don't ask: a Warrior with public setHealth() isn't a warrior, it's "
                            + "a struct with a sword-shaped name. If an outside class is doing the "
                            + "arithmetic your entity's own state requires, the entity is anemic no "
                            + "matter how many fields it has.")
                    .slainOn(2026, 8, 24)
                    .build(),

            Slay.builder()
                    .id("leaky-contexts")
                    .title("The Context That Wouldn't Stay Bounded")
                    .topic(TechTopic.DOMAIN_DRIVEN_DESIGN)
                    .smell("Item lived in the Loot context but got imported straight into Combat, "
                            + "because it was already right there. CombatEngine read item.dropWeight "
                            + "and item.rarityMultiplier off an equipped weapon — fields that mean "
                            + "nothing mid-battle — while the loot generator reached back and read "
                            + "item.damageBonus to balance drop tables. One class, two contexts, and "
                            + "a schema change made for either reason broke the other team's code.")
                    .theSlay("Drew the line the domain already had. Combat got its own Weapon, "
                            + "carrying only what a fight needs; Loot kept Item, carrying only what "
                            + "generation needs. A small mapper at the boundary turns a dropped Item "
                            + "into an equippable Weapon. Combat no longer knows what rarity is; Loot "
                            + "no longer knows what a hit calculation needs.")
                    .lesson("A shared entity between two bounded contexts isn't reuse, it's coupling "
                            + "wearing reuse as a costume. If two teams are editing the same class for "
                            + "unrelated reasons, the context map is missing a line — draw it, and put "
                            + "a translator on the seam even when it feels like writing the same class "
                            + "twice.")
                    .slainOn(2026, 8, 17)
                    .build()

    );

    @Override
    public List<Slay> findAll() {
        return SLAYS;
    }
}
