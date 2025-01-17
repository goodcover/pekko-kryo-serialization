/**
 * *****************************************************************************
 * Copyright 2012 Roman Levenstein
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
 * ****************************************************************************
 */

package io.altoo.pekko.serialization.kryo.compat.serializer

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import org.apache.pekko.actor.typed.*

/**
 * Specialized serializer for typed actor refs.
 *
 * @author Arman Bilge
 */
class CompatTypedActorRefSerializer(val system: ActorSystem[Nothing]) extends Serializer[ActorRef[Nothing]] {

  private val resolver = ActorRefResolver(system)

  override def read(kryo: Kryo, input: Input, typ: Class[_ <: ActorRef[Nothing]]): ActorRef[Nothing] = {
    val path = input.readString()
    val newPath = path.replace("akka://", "pekko://")
    resolver.resolveActorRef(newPath)
  }

  override def write(kryo: Kryo, output: Output, obj: ActorRef[Nothing]): Unit = {
    output.writeAscii(resolver.toSerializationFormat(obj))
  }
}
