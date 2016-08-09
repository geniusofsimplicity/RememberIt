package com.example.paul.rememberit.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Paul on 26.06.2016.
 */
public class Teacher {

    private int wordNumber = 0;
    private List<Card> cardsForOneLesson;
    private boolean isPrevCardSaved;
    private Card card;
    private boolean revisionMode = false;
    private boolean readyToSaveLessonResult = false;

    public Teacher(List<Card> cardsForOneLesson) {
        this.cardsForOneLesson = cardsForOneLesson;
    }

    public Teacher() {
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public List<Card> getCardsForOneLesson(){
        return cardsForOneLesson;
    }

    public Card nextCard(){
        isPrevCardSaved = false;
        if (revisionMode){
            wordNumber = searchNextCardForRevision(wordNumber);
        }
        card = cardsForOneLesson.get(wordNumber);
        return card;
    }

    public boolean hasNextCard(){
        if ((searchNextCardForRevision(0) >= 0 && revisionMode) || (!revisionMode)){
            return true;
        }else{
            return false;
        }
    }

    private int searchNextCardForRevision(int start){
        int i = start, result = -1;
        while (i < cardsForOneLesson.size() && result < 0){
            Card c = cardsForOneLesson.get(i);
            if (c.progress == 0){
                result = i;
            }
            i++;
        }
        return result;
    }

    public void saveCard(int difficulty){//0=easy, 1=medium, 2=hard
        int progress = card.progress;
        switch (difficulty){
            case 0://easy
                progress++;

                break;
            case 1://medium
                if (revisionMode){
                    progress++;
                }
                break;
            case 2://hard
                if (progress > 0){
                    progress--;
                }
                break;
            default:
                break;
        }

        card.progress = progress;
        card.setLastview();
        card.setNextReview();
        cardsForOneLesson.set(wordNumber, card);
        wordNumber++;
        if (wordNumber >= cardsForOneLesson.size()){
            revisionMode = true;
            wordNumber = 0;
        }
    }

    public void saveCardLearningResult(){

    }

    public static ArrayList<ProgressRow> prepareWordsForLesson(ArrayList<ProgressRow> poolOfExamples, ArrayList<Long> progressList, int maxPerDay){
        ArrayList<ProgressRow> resultExamplesIds = new ArrayList<ProgressRow>();
        long wordAmount = poolOfExamples.size();
        int exampleNumber;

        for (int i = 0; i < maxPerDay && i < wordAmount; i++){
            exampleNumber = nextProbableWord(progressList);
            resultExamplesIds.add(poolOfExamples.get(exampleNumber));
            progressList.remove(exampleNumber);
            poolOfExamples.remove(exampleNumber);
        }
        return resultExamplesIds;
    }

    private static int nextProbableWord(ArrayList<Long> progress){
        int pQuantity = progress.size();
        ArrayList<Long> progressSorted = (ArrayList<Long>)progress.clone();
        Collections.sort(progressSorted);
        long maxId = progress.get(progressSorted.size()-1);
        double r;
        List<Double> progressNorm = new ArrayList<Double>();
        for(Long l: progress){
            progressNorm.add( ((double)l/maxId));
        }
        double[] accumSum = new double[pQuantity];
        accumSum[0] = progressNorm.get(0);
        int i, j, k;
        for(i = 1; i < pQuantity; i++){
            accumSum[i] = accumSum[i-1] + progressNorm.get(i);
        }
        Random random = new Random();
        r = random.nextDouble();
        r = r*accumSum[pQuantity-1];
        for(k = 0; r > accumSum[k]; k++);
        return k;
    }

}
