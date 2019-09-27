package com.vincentcho.transformer.vo

import com.google.gson.annotations.SerializedName

data class Transformer(@SerializedName("id")var id: String = "",
                       @SerializedName("team")var team: String = "",
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

    fun getAttrName(position: Int): String{
        when (position) {
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

    fun partialJson(): String {
        val transformerStr = "{\"name\": \"$name\", \"strength\": $strength, \"intelligence\": $intelligence, \"speed\": $speed, \"endurance\": $endurance, \"rank\": $rank, \"courage\": $courage, \"firepower\": $firepower, \"skill\": $skill, \"team\": \"$team\"}"
        return transformerStr
    }

    fun partialJsonForUpdate(): String {
        val transformerStr = "{\"id\": \"$id\", \"name\": \"$name\", \"strength\": $strength, \"intelligence\": $intelligence, \"speed\": $speed, \"endurance\": $endurance, \"rank\": $rank, \"courage\": $courage, \"firepower\": $firepower, \"skill\": $skill, \"team\": \"$team\"}"
        return transformerStr
    }

    fun overallRating(): Int {
        return (strength + intelligence + speed + endurance + firepower)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Transformer

        if (this.id == other.id) return true

        return false
    }
}

