package io.exercism.analyzer.exercises.leap

import scala.meta._

/**
 * Extractor objects to simplify matching on tokens.
 */
object TokenExtractors {
  object SimpleLeapExpression {
    final def unapply(arg: Term.ApplyInfix): Option[Term.ApplyInfix] =  {
      arg match {
        case q"${_: Term.Name} % 4 == 0 && (${_: Term.Name} % 100 != 0 || ${_: Term.Name} % 400 == 0)" => Some(arg)
        case q"${_: Term.Name} % 4 == 0 && ${_: Term.Name} % 100 != 0 || ${_: Term.Name} % 400 == 0" => Some(arg)
        case q"${_: Term.Name} % 4 == 0 && (${_: Term.Name} % 400 == 0 || ${_: Term.Name} % 100 != 0)" => Some(arg)
        case q"(${_: Term.Name} % 4 == 0 && !(${_: Term.Name} % 100 == 0)) || ${_: Term.Name} % 400 == 0" => Some(arg)
        case q"${_: Term.Name} % 400 == 0 || (${_: Term.Name} % 100 != 0 && ${_: Term.Name} % 4 == 0)" => Some(arg)
        case _ => None
      }
    }
  }

  /**
   * Matches if a function is being called to perform mod operations.
   */
  object CallingFunction {
    final def unapply(arg: Term.ApplyInfix): Option[Term.ApplyInfix] = {
      arg match {
        case q"${_: Term.Name}(4) && (${_: Term.Name}(400) || !${_: Term.Name}(100))" => Some(arg)
        case q"${_: Term.Name}(4) && (!${_: Term.Name}(100) || ${_: Term.Name}(400))" => Some(arg)
        case q"${_: Term.Name}(4) && !${_: Term.Name}(100) || ${_: Term.Name}(400)" => Some(arg)
        case q"${_: Term.Name}(400) || (${_: Term.Name}(4) && !${_: Term.Name}(100))" => Some(arg)
        case q"${_: Term.Name}(${_: Term.Name}, 4) && (!${_: Term.Name}(${_: Term.Name}, 100) || ${_: Term.Name}(${_: Term.Name}, 400))"
          => Some(arg)
        case q"${_: Term.Name}(${_: Term.Name}, 4) && (${_: Term.Name}(${_: Term.Name}, 400) || !${_: Term.Name}(${_: Term.Name}, 100))"
          => Some(arg)
        case q"(${_: Term.Name}(${_: Term.Name}, 400) || !${_: Term.Name}(${_: Term.Name}, 100)) && ${_: Term.Name}(${_: Term.Name}, 4)"
          => Some(arg)
        case q"(!${_: Term.Name}(${_: Term.Name}, 100) || ${_: Term.Name}(${_: Term.Name}, 400)) && ${_: Term.Name}(${_: Term.Name}, 4)"
          => Some(arg)
        case q"(${_: Term.Name}(${_: Term.Name}, 4) && !${_: Term.Name}(${_: Term.Name}, 100)) || ${_: Term.Name}(${_: Term.Name}, 400)"
          => Some(arg)
        case _ => None
      }
    }
  }

  /**
   * Matches if `leapYear` function has a Boolean return type
   */
  object BooleanReturn {
    def unapply(arg: Type.Name): Option[Type.Name] = {
      arg match {
        case Type.Name("Boolean") => arg.parent match {
          case Some(Defn.Def(_, Term.Name("leapYear"), _, _, _, _)) => Some(arg)
          case _ => None
        }
        case _ => None
      }
    }
  }
}
