package com.example.chatclient;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_signIn), withText("SignIn"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("abhi@g.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.Password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("12345678"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.signin), withText("Sign In"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.usersList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        imageView.perform(scrollTo(), click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.grpbtn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.usersList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(0);
        materialTextView2.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        appCompatImageView2.perform(scrollTo(), click());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.add),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText.perform(replaceText("bryan@g.com"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.grpbtn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView5.perform(click());

        DataInteraction materialTextView3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.usersList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(0);
        materialTextView3.perform(click());

        ViewInteraction appCompatImageView6 = onView(
                allOf(withId(R.id.addGrp),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                2)));
        appCompatImageView6.perform(scrollTo(), click());

        ViewInteraction editText2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText2.perform(replaceText("bryan@g.com"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction appCompatImageView7 = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        appCompatImageView7.perform(scrollTo(), click());

        ViewInteraction appCompatImageView8 = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView8.perform(click());

        DataInteraction materialTextView4 = onData(anything())
                .inAdapterView(allOf(withId(R.id.usersList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(2);
        materialTextView4.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.textSend),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1)));
        editText3.perform(scrollTo(), replaceText("hi"), closeSoftKeyboard());

        pressBack();

        ViewInteraction button = onView(
                allOf(withId(R.id.sendComment), withText(">"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                2)));
        button.perform(scrollTo(), click());

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        imageView2.perform(scrollTo(), click());

        ViewInteraction appCompatImageView9 = onView(
                allOf(withId(R.id.ProfileImage),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView9.perform(click());

        pressBack();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
