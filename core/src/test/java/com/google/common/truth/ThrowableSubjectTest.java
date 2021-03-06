/*
 * Copyright (c) 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.truth;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Throwable} subjects.
 *
 * @author Kurt Alfred Kluever
 */
@RunWith(JUnit4.class)
public class ThrowableSubjectTest {
  @Test
  public void hasMessage() {
    NullPointerException npe = new NullPointerException("message");
    assertThat(npe).hasMessage("message");
  }

  @Test
  public void hasMessageThat() {
    NullPointerException npe = new NullPointerException("message");
    assertThat(npe).hasMessageThat().isEqualTo("message");
  }

  @Test
  public void hasMessageThat_null() {
    assertThat(new NullPointerException()).hasMessageThat().isNull();
    assertThat(new NullPointerException(null)).hasMessageThat().isNull();
  }

  @Test
  public void hasMessageThat_failure() {
    NullPointerException actual = new NullPointerException("message");
    try {
      assertThat(actual).hasMessageThat().isEqualTo("foobar");
      throw new Error("Expected to fail.");
    } catch (ComparisonFailure expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for java.lang.NullPointerException: "
                  + "expected:<[foobar]> but was:<[message]>");
      assertErrorHasActualAsCause(actual, expected);
    }
  }

  @Test
  public void hasMessageThat_MessageHasNullMessage_failure() {
    try {
      assertThat(new NullPointerException("message")).hasMessageThat().isNull();
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for java.lang.NullPointerException: "
                  + "Not true that <\"message\"> is null");
    }
  }

  @Test
  public void hasMessageThat_Named_failure() {
    try {
      assertThat(new NullPointerException("message"))
          .named("NPE")
          .hasMessageThat()
          .isEqualTo("foobar");
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for NPE(java.lang.NullPointerException): "
                  + "expected:<[foobar]> but was:<[message]>");
    }
  }

  @Test
  public void hasMessageThat_NullMessageHasMessage_failure() {
    try {
      assertThat(new NullPointerException(null)).hasMessageThat().isEqualTo("message");
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for java.lang.NullPointerException: "
                  + "Not true that <null> is equal to <\"message\">");
    }
  }

  @Test
  public void hasCauseThat_message() {
    assertThat(new Exception("foobar", new IOException("barfoo")))
        .hasCauseThat()
        .hasMessageThat()
        .isEqualTo("barfoo");
  }

  @Test
  public void hasCauseThat_instanceOf() {
    assertThat(new Exception("foobar", new IOException("barfoo")))
        .hasCauseThat()
        .isInstanceOf(IOException.class);
  }

  @Test
  public void hasCauseThat_null() {
    assertThat(new Exception("foobar")).hasCauseThat().isNull();
  }

  @Test
  public void hasCauseThat_message_failure() {
    Exception actual = new Exception("foobar", new IOException("barfoo"));
    try {
      assertThat(actual).hasCauseThat().hasMessageThat().isEqualTo("message");
      throw new Error("Expected to fail.");
    } catch (ComparisonFailure expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected cause for java.lang.Exception: Unexpected message for "
                  + "java.io.IOException: expected:<[message]> but was:<[barfoo]>");
      assertErrorHasActualAsCause(actual, expected);
    }
  }

  @Test
  public void hasCauseThat_instanceOf_failure() {
    Exception actual = new Exception("foobar", new IOException("barfoo"));
    try {
      assertThat(actual).hasCauseThat().isInstanceOf(RuntimeException.class);
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected cause for java.lang.Exception: Not true that <java.io.IOException: "
                  + "barfoo> is an instance of <java.lang.RuntimeException>. "
                  + "It is an instance of <java.io.IOException>");
      assertErrorHasActualAsCause(actual, expected);
    }
  }

  @Test
  public void hasCauseThat_tooDeep_failure() {
    Exception actual = new Exception("foobar");
    try {
      assertThat(actual).hasCauseThat().hasCauseThat().isNull();
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected cause for java.lang.Exception: "
                  + "Causal chain is not deep enough - add a .isNotNull() check?");
      assertErrorHasActualAsCause(actual, expected);
    }
  }

  @Test
  public void hasCauseThat_deepNull_failure() {
    Exception actual =
        new Exception("foobar", new RuntimeException("barfoo", new IOException("buzz")));
    try {
      assertThat(actual).hasCauseThat().hasCauseThat().hasMessageThat().isEqualTo("message");
      throw new Error("Expected to fail.");
    } catch (ComparisonFailure expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected cause for java.lang.Exception: Unexpected cause for "
                  + "java.lang.RuntimeException: Unexpected message for java.io.IOException: "
                  + "expected:<[message]> but was:<[buzz]>");
      assertErrorHasActualAsCause(actual, expected);
    }
  }

  @Test
  public void inheritedMethodChainsSubject() {
    NullPointerException expected = new NullPointerException("expected");
    NullPointerException actual = new NullPointerException("actual");
    try {
      assertThat(actual).isEqualTo(expected);
      throw new Error("Expected to fail.");
    } catch (AssertionError thrown) {
      assertErrorHasActualAsCause(actual, thrown);
    }
  }

  private static void assertErrorHasActualAsCause(Throwable actual, AssertionError failure) {
    assertThat(failure.getCause()).named("AssertionError's cause").isEqualTo(actual);
  }
}
