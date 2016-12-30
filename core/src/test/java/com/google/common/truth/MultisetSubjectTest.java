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
import static org.junit.Assert.fail;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for Multiset Subjects.
 *
 * @author Kurt Alfred Kluever
 */
@RunWith(JUnit4.class)
public class MultisetSubjectTest {
  @Test
  public void multisetIsEmpty() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of();
    assertThat(multiset).isEmpty();
  }

  @Test
  public void multisetIsEmptyWithFailure() {
    ImmutableMultiset<Integer> multiset = ImmutableMultiset.of(1, 5);
    try {
      assertThat(multiset).isEmpty();
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat().isEqualTo("Not true that <[1, 5]> is empty");
    }
  }

  @Test
  public void multisetIsNotEmpty() {
    ImmutableMultiset<Integer> multiset = ImmutableMultiset.of(1, 5);
    assertThat(multiset).isNotEmpty();
  }

  @Test
  public void multisetIsNotEmptyWithFailure() {
    ImmutableMultiset<Integer> multiset = ImmutableMultiset.of();
    try {
      assertThat(multiset).isNotEmpty();
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat().isEqualTo("Not true that <[]> is not empty");
    }
  }

  @Test
  public void hasSize() {
    assertThat(ImmutableMultiset.of(1, 2, 3, 4)).hasSize(4);
  }

  @Test
  public void hasSizeZero() {
    assertThat(ImmutableMultiset.of()).hasSize(0);
  }

  @Test
  public void hasSizeNegative() {
    try {
      assertThat(ImmutableMultiset.of(1, 2)).hasSize(-1);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void hasCount() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kurt", "kluever");
    assertThat(multiset).hasCount("kurt", 2);
    assertThat(multiset).hasCount("kluever", 1);
    assertThat(multiset).hasCount("alfred", 0);

    assertThat(multiset).named("name").hasCount("kurt", 2);
  }

  @Test
  public void hasCountFail() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kurt", "kluever");
    try {
      assertThat(multiset).hasCount("kurt", 3);
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessageThat()
          .isEqualTo(
              "Not true that <[kurt x 2, kluever]> has a count for <kurt> of <3>. It is <2>");
      return;
    }
    fail("Should have thrown.");
  }

  @Test
  public void contains() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kluever");
    assertThat(multiset).contains("kurt");
  }

  @Test
  public void containsFailure() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kluever");
    try {
      assertThat(multiset).contains("greg");
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat().isEqualTo("<[kurt, kluever]> should have contained <greg>");
      return;
    }
    fail("Should have thrown.");
  }

  @Test
  public void containsNullFailure() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kluever");
    try {
      assertThat(multiset).contains(null);
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat().isEqualTo("<[kurt, kluever]> should have contained <null>");
      return;
    }
    fail("Should have thrown.");
  }

  @Test
  public void containsNull() {
    Multiset<String> multiset = HashMultiset.create();
    multiset.add(null);
    assertThat(multiset).contains(null);
  }

  @Test
  public void doesNotContain() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kluever");
    assertThat(multiset).doesNotContain("greg");
    assertThat(multiset).doesNotContain(null);
  }

  @Test
  public void doesNotContainFailure() {
    ImmutableMultiset<String> multiset = ImmutableMultiset.of("kurt", "kluever");
    try {
      assertThat(multiset).doesNotContain("kurt");
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e)
          .hasMessageThat()
          .isEqualTo("<[kurt, kluever]> should not have contained <kurt>");
    }
  }

  @Test
  public void doesNotContainNull() {
    Multiset<String> multiset = HashMultiset.create();
    multiset.add(null);
    try {
      assertThat(multiset).doesNotContain(null);
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e).hasMessageThat().isEqualTo("<[null]> should not have contained <null>");
    }
  }
}
