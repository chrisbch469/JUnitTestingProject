package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by chris on 8/11/2016.
 */
public class UserTest {

    private Board board;
    private User userQuestion, userAnswer, userOther;

    @Rule
    public ExpectedException exc = ExpectedException.none();

    //arrange objects
    @Before
    public void setUp() throws Exception {
      board = new Board("Topic");
        userQuestion = board.createUser("Questioner");
        userAnswer = board.createUser(("Answerer"));
        userOther = board.createUser("other");
    }

    @Test
    public void questionersReputationGoesUpByFiveIfUpvoted() throws Exception{

        //initial userQuestion reputation
        int questionerReputation = userQuestion.getReputation();

        //ask question
        Question question = userQuestion.askQuestion("question");

        //other user will upvote
        userOther.upVote(question);

        //assert that the reputation is 5 pts larger

        assertEquals(questionerReputation + 5, userQuestion.getReputation());

    }

    @Test
    public void answerersReputationGoesUpByTenIfUpvoted() throws Exception{

        //initial userAnswer reputation
        int answererReputation = userAnswer.getReputation();

        //create question and answer question
        Question question = userQuestion.askQuestion("question");
        Answer answer = userAnswer.answerQuestion(question, "answer");
        userOther.upVote(answer); //upvote answer

        //assert that answerers reputation is 10 pts larger
        assertEquals(answererReputation + 10, userAnswer.getReputation());

    }

    @Test
    public void answerersReputationGoesUpByFifteenIfAccepted() throws Exception{

        //intitial userAnswer reputation
        int answersRepuation = userAnswer.getReputation();

        //create question and answer question and accept question
        Question question = userQuestion.askQuestion("question");
        Answer answer = userAnswer.answerQuestion(question, "answer");
        userQuestion.acceptAnswer(answer);

        //assert that answerers reputation is 15 pts larger
        assertEquals(answersRepuation + 15, userAnswer.getReputation());

    }

    @Test
    public void userUpvotingTheirOwnQuestionIsNotAllowed() throws Exception{
        //expected exception from user (voting exception);
        exc.expect(VotingException.class);
        exc.expectMessage("You cannot vote for yourself!");

        Question question = userQuestion.askQuestion("question"); //ask question
        userQuestion.upVote(question); //upvote own question

    }

    @Test
    public void userUpvotingTheirOwnAnswerIsNotAllowed() throws Exception{
        exc.expect(VotingException.class);
        exc.expectMessage("You cannot vote for yourself!");

        Question question = userQuestion.askQuestion("question"); //question is asked
        Answer answer = userAnswer.answerQuestion(question, "answer"); //user answers question
        userAnswer.upVote(answer);  //ueser tries to upvote their own answer

    }

    @Test
    public void verifyingThatQuestionCanOnlyBeAcceptedByTheOriginalQuestioner() throws Exception{
        exc.expect(AnswerAcceptanceException.class);
        exc.expectMessage("Only " + userQuestion.getName() + " can accept this answer as it is their question");

        Question question = userQuestion.askQuestion("question"); //user asks question
        Answer answer = userAnswer.answerQuestion(question, "answer"); //user answers question
        userAnswer.acceptAnswer(answer); //answerer tries to accept answer

    }

    @Test
    public void verifyingThatReputationIsCalculatedProperly() throws Exception{

        Question question = userQuestion.askQuestion("question"); //user asks question
        userOther.upVote(question);     //user upvotes question (rep - 5)
        userAnswer.downVote(question); //user downvotes question (rep - 1)

        assertEquals("rep", 4 , userQuestion.getReputation());


    }


}