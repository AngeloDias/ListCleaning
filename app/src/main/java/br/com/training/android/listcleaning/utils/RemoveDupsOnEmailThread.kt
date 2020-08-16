package br.com.training.android.listcleaning.utils

class RemoveDupsOnEmailThread {

    companion object {

        fun removeDupsUsingPointers(linkedList: SinglyLinkedList<String>): SinglyLinkedList<String> {
            var current = linkedList.head

            while(current != null) {
                var runner = current

                while (runner!!.next != null) {
                    if(current.value == runner.next!!.value) {

                        runner.next = runner.next!!.next
                    } else {
                        runner = runner.next
                    }

                }

                current = current.next
            }

            return linkedList
        }
    }

}