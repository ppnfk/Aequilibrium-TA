package com.vincentcho.transformer.viewmodel

import androidx.lifecycle.ViewModel
import com.vincentcho.transformer.repo.TransformerRepo
import androidx.lifecycle.LiveData
import com.vincentcho.transformer.api.SingleLiveEvent
import com.vincentcho.transformer.vo.Transformer
import java.lang.Math.min

class TransformerViewModel(val transformerRepo: TransformerRepo): ViewModel() {

    private lateinit var newTransformer: Transformer
    private var token = ""

    init {
        transformerRepo.connect()
    }

    fun goToWar() : String {
        var retString = ""
        var transformers =  transformerRepo.loadTransformers()
        var teamA = mutableListOf<Transformer>()
        var teamD = mutableListOf<Transformer>()

        var resultteamA = mutableListOf<Transformer>()
        var resultteamD = mutableListOf<Transformer>()

        for (t in transformers.value!!) {
            when (t.team) {
                "A" -> {   insertSort(teamA, t)}
                "D" -> {   insertSort(teamD, t)}
            }
        }

        var battleTimes = min(teamA.size, teamD.size)
        var aWins = 0
        var dWins = 0
        var allDead = false
        for (i in 0..battleTimes-1) {
            if ((teamA[i].name == "Optimus Prime" || teamA[i].name == "Predaking")
                && (teamD[i].name == "Optimus Prime" || teamD[i].name == "Predaking")){
                // all dead
                //deleteTransformer(teamA[i])
                //deleteTransformer(teamD[i])
                allDead = true
                break
            } else if (teamA[i].name == "Optimus Prime" || teamA[i].name == "Predaking") {
                //deleteTransformer(teamD[i])
                resultteamA.add(teamA[i])
                aWins += 1
            } else if (teamD[i].name == "Optimus Prime" || teamD[i].name == "Predaking") {
                //deleteTransformer(teamA[i])
                resultteamD.add(teamD[i])
                dWins += 1
            } else if (teamA[i].courage + 4 <= teamD[i].courage || teamA[i].strength + 3 <= teamD[i].strength){
                resultteamA.add(teamA[i])
                resultteamD.add(teamD[i])
                if (!(teamD[i].courage + 4 <= teamA[i].courage || teamD[i].strength + 3 <= teamA[i].strength)) {
                    dWins += 1
                }
            } else if (teamD[i].courage + 4 <= teamA[i].courage || teamD[i].strength + 3 <= teamA[i].strength){
                resultteamA.add(teamA[i])
                resultteamD.add(teamD[i])
                aWins += 1
            } else if (teamA[i].skill + 3 <= teamD[i].skill) {
                resultteamD.add(teamD[i])
                dWins += 1
            } else if (teamD[i].skill + 3 <= teamA[i].skill) {
                resultteamA.add(teamA[i])
                aWins += 1
            } else if (teamA[i].overallRating() > teamD[i].overallRating()) {
                resultteamA.add(teamA[i])
                aWins += 1
            } else if (teamA[i].overallRating() < teamD[i].overallRating()) {
                resultteamD.add(teamD[i])
                dWins += 1
            } else {
                // both dead
            }
        }

        if (allDead) {
            retString = "All Dead"
        } else if (battleTimes == 0) {
            retString = "$battleTimes battles \nSurvivors from team (Decepticons): "
            for (_t in teamD) {
                retString = retString + _t.name + ", "
            }

            retString.trimMargin(", ")
            retString += "\nSurvivors from team (Autobots):"
            for (_t in teamA) {
                retString = retString + _t.name + ", "
            }

            retString.trimMargin(", ")
        } else if (aWins > dWins){
            retString = "$battleTimes battles \nWinning team (Autobots): "
            for (_t in resultteamA) {
                retString = retString + _t.name + ", "
            }

            for (i in battleTimes..teamA.size-1) {
                retString = retString + teamA[i].name + ", "
            }

            retString.trimMargin(", ")
            retString += "\nSurvivors from the losing team (Decepticons):"
            for (_t in resultteamD) {
                retString = retString + _t.name + ", "
            }

            for (i in battleTimes..teamD.size-1) {
                retString = retString + teamD[i].name + ", "
            }
            retString.trimMargin(", ")

        } else if (aWins < dWins) {
            retString = "$battleTimes battles \nWinning team (Decepticons): "
            for (_t in resultteamD) {
                retString = retString + _t.name + ", "
            }

            for (i in battleTimes..teamD.size-1) {
                retString = retString + teamD[i].name + ", "
            }

            retString.trimMargin(", ")
            retString += "\nSurvivors from the losing team (Autobots):"
            for (_t in resultteamA) {
                retString = retString + _t.name + ", "
            }

            for (i in battleTimes..teamA.size-1) {
                retString = retString + teamA[i].name + ", "
            }
            retString.trimMargin(", ")
        } else {
            retString = "$battleTimes battles \nSurvivors from team (Decepticons): "
            for (_t in resultteamD) {
                retString = retString + _t.name + ", "
            }

            for (i in battleTimes..teamD.size-1) {
                retString = retString + teamD[i].name + ", "
            }

            retString.trimMargin(", ")
            retString += "\nSurvivors from team (Autobots):"
            for (_t in resultteamA) {
                retString = retString + _t.name + ", "
            }

            for (i in battleTimes..teamA.size-1) {
                retString = retString + teamA[i].name + ", "
            }
            retString.trimMargin(", ")
        }
        return retString
    }

    fun insertSort(team: MutableList<Transformer>, t: Transformer){
        for (i in 0..team.size-1){
            if (team[i].rank < t.rank) {
                team.add(i, t)
                return
            }
        }
        team.add(t)
    }

    fun getAllTransformers(): LiveData<List<Transformer>> {
        return transformerRepo.loadTransformers()
    }

    fun onItemClicked(item: Transformer) {
        event.value = UserAction.Edit(item)
    }

    fun newTransformer(): Transformer {
        newTransformer = Transformer()
        return newTransformer
    }

    fun deleteTransformer(t: Transformer) {
        transformerRepo.deleteTransformer(t)
    }

    fun updateTransformer(t: Transformer) {
        transformerRepo.updateTransformer(t)
    }

    fun createTransformer() {
        transformerRepo.createTransformer(newTransformer)
    }

    override fun onCleared() {
        transformerRepo.saveLocalListToDb()
    }

    var actionResultObservable: SingleLiveEvent<TransformerRepo.ActionResult> = transformerRepo.actionResult
    var event = SingleLiveEvent<UserAction>()

    sealed class UserAction {
        data class Edit(val transformer: Transformer) : UserAction()
    }
}