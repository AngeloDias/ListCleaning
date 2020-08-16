package br.com.training.android.listcleaning.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

class Node<T>(var value: T) {
    var next: Node<T>? = null
}

/**
 * [SinglyLinkedList] implemented some time ago for training and is being used here to get
 * the best behavior for the solution.
 * */
@Parcelize
class SinglyLinkedList<T>(
    override var size: Int,
    var head: @RawValue Node<T>?,
    var tail: @RawValue Node<T>?) : Collection<T>, Parcelable {

    /**
     * Iterate through the list until to find the last not null and add new node,
     * or to define the head.
     *
     * @param value The value to be inserted in the list
     * */
    fun add(value: T) {
        if(head == null) {
            head = Node(value)
            tail = head
        } else {
            tail!!.next = Node(value)
            tail = tail!!.next
        }

        size += 1
    }

    /**
     * Iterate through the list until to find the last not null and add new node,
     * or to define the head.
     *
     * @param nodeToInsert The node to be inserted in the list
     * */
    fun addNode(nodeToInsert: Node<T>?) {

        if(head == null) {
            head = nodeToInsert
            tail = head
        } else {
            tail!!.next = nodeToInsert
            tail = tail!!.next
        }

        size += 1
    }

    fun insertBeforeHead(value: T) {
        val node = Node(value)

        node.next = head
        head = node

        size += 1
    }

    /**
     * Removes the first node to match the value provided.
     *
     * @param value The value to be removed from the list if is contained in it.
     * @return The node removed from the list.
     * */
    fun removeFirstToMatch(value: T): Node<T>? {
        return if(head == null) {
            head
        } else {
            var node = head
            var previous = head

            while (node?.next != null) {
                if(node.value == value) {
                    if(node == tail) {
                        tail = previous
                    }

                    previous?.next = node.next
                    size -= 1

                    return node
                }

                previous = node
                node = node.next
            }

            null
        }

    }

    /**
     * Iterate through list until the last node. Then, remove it.
     *
     * @return The removed last node from list.
     * */
    fun removeLast(): Node<T>? {
        var node = head

        return if (node != null){
            if(node.next == null){
                head = null
                size -= 1

                return head
            }

            var previous = node

            while (node!!.next != null) {
                previous = node
                node = node.next
            }

            tail = previous
            previous?.next = node.next
            size -= 1

            node
        } else {
            null
        }
    }

    /**
     * Do the head node be null.
     * */
    fun clear() {
        head = null
        tail = null
        size = 0
    }

    /**
     * It receives an instance of [SinglyLinkedList] to add values to this list and iterate through
     * each element getting its value and creating a new [Node] in list.
     *
     * @param listToAdd The list to copied to this list.
     * @return True if the [listToAdd] has at least one element to copy, false otherwise.
     * */
    fun addAll(listToAdd: SinglyLinkedList<T>): Boolean {
        if(listToAdd.head == null) {
            return false
        }

        for(element in listToAdd) {
            add(element)
        }

        return true
    }

    override fun contains(element: T): Boolean {
        if(head == null) {
            return false
        }
        val iterator = iterator()

        while (iterator.hasNext()) {
            if(iterator.next() == element) {
                return true
            }
        }

        return false
    }

    /**
     * Get the [iterator] and iterate through the collection checking if each element is contained
     * in this collection.
     *
     * @return If all elements from elements are the same in this collection.
     * */
    override fun containsAll(elements: Collection<T>): Boolean {
        val iterator = iterator()

        while (iterator.hasNext()) {
            if(!contains(iterator.next())) {
                return false
            }
        }

        return true
    }

    /**
     * @return If the head is null.
     * */
    override fun isEmpty(): Boolean {
        return head == null
    }

    /**
     * @return This class Iterator instance.
     * */
    override fun iterator(): Iterator<T> {
        return SinglyIterator(this)
    }

    fun theNodeAt(index: Int): Node<T>? {
        if(this.isEmpty() || index < 0 || index > this.size) {
            return null
        }

        var count = 0
        var node = this.head

        while(node != null) {
            if(count == index) {
                return node
            }

            node = node.next
            count += 1
        }

        return null
    }

}

class SinglyIterator<T>(singlyLinkedList: SinglyLinkedList<T>) : Iterator<T> {
    var current: Node<T>? = null

    init {
        current = singlyLinkedList.head
    }

    /**
     * Check if the current [Node] isn't null.
     *
     * @return True if current isn't null, false otherwise.
     * */
    override fun hasNext(): Boolean {
        return current != null
    }

    /**
     * Get the current value and make the current node be the next.
     *
     * @return The value of the next [Node].
     * */
    override fun next(): T {
        val value = current!!.value
        current = current!!.next

        return value
    }

}