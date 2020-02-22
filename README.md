# Akon-OldPvP
 よりよいOldPvP

前提プラグイン: ProtocolLib

コンフィグ↓

```yml
Melee:
  disable-attack-speed: true #攻撃速度を無効にするかどうか  
  old-axe-damage: true #斧のダメージを1.8以前と同じにするかどうか  
  sword-blocking: true #剣で防御可能にするかどうか  
  shield-damage-reducation: 0.5 #盾のダメージ軽減率(1で100%)  
  disable-shield-cooldown: true #盾のクールダウンをなくすかどうか  
  disable-shield-block-sound: true #盾で攻撃を防いだ時の音を無効にするかどうか  
  disable-shield-break-sound: true #盾が壊れたときの音を無効にするかどうか  
  disable-offhand: true #オフハンドを使用不可にするかどうか  
  disable-sweep-attack: true #剣の範囲攻撃を無効にするかどうか  
  disable-damage-indicator-particle: true #殴ってダメージを与えたときに発生する黒いハートのパーティクルを非表示にするかどうか  
  old-hit-animation: true #1.9以降で起きる無敵状態のエンティティを殴ったときに腕が下がる現象を無効にするかどうか  
  AttackSounds: #プレイヤーがエンティティを殴ったときに発生するサウンドの設定  
    crit: false #クリティカルヒットのサウンドをなくすかどうか  
    knockback: false #ノックバックヒットのサウンドをなくすかどうか  
    nodamage: false #無敵状態のエンティティを殴ったときのサウンドをなくすかどうか  
    strong: false #強攻撃のサウンドをなくすかどうか  
    weak: false #弱攻撃のサウンドをなくすかどうか  
Projectiles:  
  egg-knockback: true #プレイヤーが卵でノックバックするかどうか  
  ender-pearl-knockback: true #プレイヤーがエンダーパールでノックバックするかどうか  
  fishing-rod-knockback: true #モブが釣り竿でノックバックするかどうか  
  snowball-knockback: true #プレイヤーが雪玉でノックバックするかどうか  
  disable-ender-pearl-cooldown: true #エンダーパールのクールダウンをなくすかどうか  
GoldenApple:  
  old-god-apple-effects: true #エンチャントされた金のリンゴのエフェクト効果を1.8以前と同じにするかどうか  
  god-apple-recipe: true #エンチャントされた金のリンゴをクラフト可能にするかどうか(1.8以前で使用可能だったレシピと同じです)  
PotionEffects:  
  old-strength: true #攻撃力上昇の効果を1.8以前と同じにするかどうか  
  old-weakness: true #弱体化の効果を1.8以前と同じにするかどうか  
Enchantments:  
  old-sharpness: true #ダメージ増加エンチャントの効果を1.8以前と同じにするかどうか  
Utilities:  
  disable-collision: true #プレイヤーのあたり判定をなくすかどうか  
  old-brewing-stand: true #ブレイズパウダーなしで醸造台を使用可能にするかどうか  
  no-lapis-enchantments: true #ラピスラズリなしでエンチャントテーブルを使用可能にするかどうか  
  custom-player-damage-sound:  
    enabled: false #プレイヤーのダメージサウンドを置き換えるかどうか  
    sound: ENTITY_BLAZE_HURT #プレイヤーのダメージサウンド(サウンドの一覧: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html)
```
