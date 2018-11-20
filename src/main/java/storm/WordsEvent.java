/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storm;


      public  class WordsEvent {

            private int id;
            private String word;

            public WordsEvent(int id1, String word1) {
                this.id = id1;
                this.word = word1;
            }
            
            public long getid() {
                    return this.id;
            }
            public String getword() {
                    return this.word;
            }
            
            
        }
