package com.gghouse.wardah.wardahba.dummy;

import com.gghouse.wardah.wardahba.enumeration.UserTypeEnum;
import com.gghouse.wardah.wardahba.model.Answer;
import com.gghouse.wardah.wardahba.model.Location;
import com.gghouse.wardah.wardahba.model.Pelanggan;
import com.gghouse.wardah.wardahba.model.ProductHighlight;
import com.gghouse.wardah.wardahba.model.Question;
import com.gghouse.wardah.wardahba.model.Questioner;
import com.gghouse.wardah.wardahba.model.Sales;
import com.gghouse.wardah.wardahba.model.SalesHistoryHeader;
import com.gghouse.wardah.wardahba.model.Test;
import com.gghouse.wardah.wardahba.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.reflect.jvm.internal.impl.protobuf.ProtocolStringList;

public class WardahDummy {
    public static final User user;

    public static final List<Test> TEST_SIMPLE = new ArrayList<Test>();
    public static final List<Test> TEST_HISTORY = new ArrayList<Test>();

    public static final List<Sales> SALES_SIMPLE = new ArrayList<Sales>();
    public static final List<Sales> SALES_HISTORY = new ArrayList<Sales>();

    public static final List<Pelanggan> PELANGGAN_SIMPLE = new ArrayList<Pelanggan>();
    public static final List<Pelanggan> PELANGGAN_HISTORY = new ArrayList<Pelanggan>();

    public static final List<Questioner> QUESTIONER_SIMPLE = new ArrayList<Questioner>();

    public static final List<Question> QUESTION_SIMPLE = new ArrayList<Question>();

    public static final List<ProductHighlight> PRODUCT_HIGHLIGHT_SIMPLE = new ArrayList<ProductHighlight>();

    private static final int SIMPLE_COUNT = 5;
    private static final int HISTORY_COUNT = 10;

    static {
        user = new User();
        user.setId(1L);
        user.setLastQuestionId(1L);
        user.setFullName("Michael Halim");
        user.setEmail("mikefla10@gmail.com");
        user.setMobileNumber("081224522764");
        user.setDateOfBirth(655121400000L);
        Location location = new Location();
        location.setId(1L);
        location.setName("Wisma Bumi Putera");
        location.setAddress("Jl.Asia Afrika");
        location.setCity("Bandung");
        location.setProvince("Jawa Barat");
        user.setLocation(location);
        user.setUserType(UserTypeEnum.BEAUTY_PROMOTER_LEADER.toString());

        SALES_HISTORY.add(new SalesHistoryHeader());

        for (int i = 0; i < SIMPLE_COUNT; i++) {
            List<Answer> answerList = new ArrayList<Answer>();
            for (int j = 0; j < SIMPLE_COUNT; j++) {
                answerList.add(new Answer(i + j, "Jawaban " + j));
            }
            QUESTION_SIMPLE.add(new Question(i, "Pertanyaan ke " + i, answerList));

            TEST_SIMPLE.add(new Test(100, "Tes ke " + (i + 1), i + i, new Date().getTime()));
            QUESTIONER_SIMPLE.add(new Questioner((long) i, "Soal: " + i));

            PRODUCT_HIGHLIGHT_SIMPLE.add(new ProductHighlight((long) i, "Name: " + i));
        }

        for (long i = 0; i < SIMPLE_COUNT; i++) {
            SALES_SIMPLE.add(new Sales(new Date().getTime(), i, (Double.parseDouble(i + "") + 1000000), i, i == 0 ? true : false));
            PELANGGAN_SIMPLE.add(new Pelanggan(i, "Name " + (i + 1), "name" + (i + 1) + "@mmail.com"));
        }

        for (int i = 0; i < HISTORY_COUNT; i++) {
            TEST_HISTORY.add(new Test(100, "Tes ke " + (i + 1), i + i, new Date().getTime()));
        }

        for (long i = 0; i < HISTORY_COUNT; i++) {
            SALES_HISTORY.add(
                    new Sales(
                            new Date().getTime() - (i * (1000 * 60 * 60 * 24)),
                            i,
                            (Double.parseDouble(i + "") + 1000000),
                            i,
                            i == 0 ? true : false)
            );

            PELANGGAN_HISTORY.add(new Pelanggan(i, "Name " + (i + 1), "name" + (i + 1) + "@mmail.com"));
        }
    }
}
