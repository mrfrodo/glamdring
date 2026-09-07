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
