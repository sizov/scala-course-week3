package objsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TweetSetSuite extends FunSuite {

  trait TestSets {
    val set1 = new Empty
    val set2 = set1.incl(new Tweet("a", "a body", 20))
    val set3 = set2.incl(new Tweet("b", "b body", 20))
    val c = new Tweet("c", "c body", 7)
    val d = new Tweet("d", "d body", 9)
    val set4c = set3.incl(c)
    val set4d = set3.incl(d)
    val set5 = set4c.incl(d)
  }

  trait TestSets1 {
    val set1 = new Empty
    val set2 = set1.incl(new Tweet("a", "a body", 20))
    val set3 = set2.incl(new Tweet("b", "b body", 25))
    val set4 = set3.incl(new Tweet("c", "c body", 3))
  }

  def asSet(tweets: TweetSet): Set[Tweet] = {
    var res = Set[Tweet]()
    tweets.foreach(res += _)
    res
  }

  def size(set: TweetSet): Int = asSet(set).size

  test("filter: on empty set") {
    new TestSets {
      private val filter: TweetSet = set1.filter(tw => tw.user == "a")
      println(filter)
      assert(size(filter) === 0)
    }
  }

  test("filter: a on set5") {
    new TestSets {
      private val filter: TweetSet = set5.filter(tw => tw.user == "a")
      println(filter)
      assert(size(filter) === 1)
    }
  }

  test("filter: 20 on set5") {
    new TestSets {
      private val filter: TweetSet = set5.filter(tw => tw.retweets == 20)
      println(filter)
      assert(size(filter) === 2)
    }
  }

  test("union: set4c and set4d") {
    new TestSets {
      assert(size(set4c.union(set4d)) === 4)
    }
  }

  test("union: with empty set (1)") {
    new TestSets {
      assert(size(set5.union(set1)) === 4)
    }
  }

  test("union: with empty set (2)") {
    new TestSets {
      assert(size(set1.union(set5)) === 4)
    }
  }

  test("most retweeted: (1)") {
    new TestSets1 {
      assert(set2.mostRetweeted.text === "a body")
      assert(set2.mostRetweeted.user === "a")
    }
  }

  test("most retweeted: (2)") {
    new TestSets1 {
      assert(set3.mostRetweeted.text === "b body")
      assert(set3.mostRetweeted.user === "b")
    }
  }

  test("most retweeted: (3)") {
    new TestSets1 {
      assert(set4.mostRetweeted.text === "b body")
      assert(set4.mostRetweeted.user === "b")
    }
  }

  test("most retweeted: calculating maxretweeted in Empty set should throw NoSuchElementException") {
    new TestSets1 {
      intercept[IndexOutOfBoundsException] {
        set1.mostRetweeted.text
      }
    }
  }

//  test("descending: set5") {
//    new TestSets {
//      val trends = set5.descendingByRetweet
//      assert(!trends.isEmpty)
//      assert(trends.head.user == "a" || trends.head.user == "b")
//    }
//  }
}
