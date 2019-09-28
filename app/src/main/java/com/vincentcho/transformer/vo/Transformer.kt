package com.vincentcho.transformer.vo

import com.google.gson.annotations.SerializedName
/**
 * Transformer
 *
 * This class has all the attributes of a transformer, has no logic;
 *
 * @property id the unique id of this transformer
 * @property team should be only "A": AutoRobot or "D": Decepticons
 * @property name name of this transformer
 * @property strength attribute Strength 1 - 10
 * @property intelligence attribute Intelligence 1 - 10
 * @property speed attribute Speed 1 - 10
 * @property endurance attribute Endurance 1 - 10
 * @property rank attribute Rank 1 - 10, it decides the battle sequences.
 * @property courage attribute Courage 1 - 10, if it is too low, the transformer may runawy while battling
 * @property firepower attribute Firepower 1 - 10
 * @property skill attribute Skill 1 - 10
 * @property teamIcon an image url which is got from game server
 * @constructor Creates an new transformer group.
 */
data class Transformer(@SerializedName("id")var id: String = "",
                       @SerializedName("team")var team: String = "A",
                       @SerializedName("name")var name: String = "",
                       @SerializedName("strength")var strength: Int = 1,
                       @SerializedName("intelligence")var intelligence: Int = 1,
                       @SerializedName("speed")var speed: Int = 1,
                       @SerializedName("endurance")var endurance: Int = 1,
                       @SerializedName("rank")var rank: Int = 1,
                       @SerializedName("courage")var courage: Int = 1,
                       @SerializedName("firepower")var firepower: Int = 1,
                       @SerializedName("skill")var skill: Int = 1,
                       @SerializedName("team_icon")var teamIcon: String = ""){

    companion object {
        const val ATTRIBUTE_SIZE = 8
    }
    /**
     * Sets a value to an attribute of this transformer
     * @param attrName name of Attribute
     * @param attrValue value of Attribute
     */
    fun setAttr(attrName: String, attrValue: Int){
        when (attrName){
            "Strength" -> strength = attrValue
            "Skill" -> skill  = attrValue
            "Rank" -> rank = attrValue
            "Firepower" -> firepower = attrValue
            "Endurance" -> endurance = attrValue
            "Intelligence" -> intelligence = attrValue
            "Courage" -> courage = attrValue
            "Speed" -> speed = attrValue
        }
    }

    /**
     * Gets a attribute name of this transformer by giving a index
     * @param attrIndex index of an attribute
     * @return the display name of this attribute
     */
    fun getAttrName(attrIndex: Int): String{
        when (attrIndex) {
            0 -> return "Strength"
            1 -> return "Intelligence"
            2 -> return "Speed"
            3 -> return "Rank"
            4 -> return "Endurance"
            5 -> return "Courage"
            6 -> return "Firepower"
            7 -> return "Skill"
        }
        return ""
    }

    /**
     * Gets a attribute value of this transformer by giving a index
     * @param attrIndex index of an attribute
     * @return the value of this attribute
     */
    fun getAttr(position: Int): Int{
        when (position) {
            0 -> return strength
            1 -> return intelligence
            2 -> return speed
            3 -> return rank
            4 -> return endurance
            5 -> return courage
            6 -> return firepower
            7 -> return skill
        }
        return 0
    }

    /**
     * Gets attributes json string of this transformer
     * @return json string of this transformer
     */
    fun attributesJson(): String {
        val transformerStr = "{\"name\": \"$name\", \"strength\": $strength, \"intelligence\": $intelligence, \"speed\": $speed, \"endurance\": $endurance, \"rank\": $rank, \"courage\": $courage, \"firepower\": $firepower, \"skill\": $skill, \"team\": \"$team\"}"
        return transformerStr
    }

    /**
     * Gets attributes json string of this transformer with its ID
     * @return json string of this transformer with its ID
     */
    fun attributesJsonWithId(): String {
        val transformerStr = "{\"id\": \"$id\", \"name\": \"$name\", \"strength\": $strength, \"intelligence\": $intelligence, \"speed\": $speed, \"endurance\": $endurance, \"rank\": $rank, \"courage\": $courage, \"firepower\": $firepower, \"skill\": $skill, \"team\": \"$team\"}"
        return transformerStr
    }

    /**
     * Gets overall rating of this transformer
     * @return overall rating value which is sum of (strength + intelligence + speed + endurance + firepower)
     */
    fun overallRating(): Int {
        return (strength + intelligence + speed + endurance + firepower)
    }

    /**
     * Override equals of this transformer, we compare its id to distinquish if it is the same transformer
     * @return true/false
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Transformer

        if (this.id == other.id) return true

        return false
    }
}

