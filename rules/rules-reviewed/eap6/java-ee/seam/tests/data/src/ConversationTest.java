package org.jboss.seam.test.integration;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.core.ConversationEntries;
import org.jboss.seam.core.ConversationEntry;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.Switcher;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

public class ConversationTest 
    extends SeamTest
{
    
    @Test
    public void conversationStack() 
        throws Exception 
    {
        // no conversation, no stack
        new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
                assert entries.size() == 0;                            
            }
        }.run();
        
       // no conversation, no stack
       new FacesRequest("/pageWithoutDescription.xhtml") {
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginConversation();
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
               assert entries.size() == 0;                            
           }
       }.run();
       
       // new conversation, stack = 1
       String rootId = new FacesRequest("/pageWithDescription.xhtml") {
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginConversation();
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
               assert entries.size() == 1;                            
           }
       }.run();

       // nested conversation, stack =2
       String nested1 = new FacesRequest("/pageWithDescription.xhtml", rootId) {
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginNestedConversation();
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");
               assert entries.size() == 2;                         
           }
                    
       }.run();
   
       // nested conversation without description, not added to stack
       String nested2 = new FacesRequest("/pageWithoutDescription.xhtml", nested1) {       
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().beginNestedConversation();
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               assert entries.size() == 2;  
           }
       }.run();
       
       // access a page, now it's on the stack
       new FacesRequest("/pageWithDescription.xhtml", nested2) {
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               assert entries.size() == 3;  
           }
       }.run();
       
       // end conversation, stack goes down
       new FacesRequest("/pageWithDescription.xhtml", nested2) {       
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().endConversation(false);
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               assert entries.size() == 2;  
           }
       }.run();
       
       // end another one, size is 1
       new FacesRequest("/pageWithDescription.xhtml", nested1) {       
           @Override
           protected void invokeApplication() throws Exception {
              Manager.instance().endConversation(false);
           }
           
           @Override
           protected void renderResponse() throws Exception {
               List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationStack}");               
               assert entries.size() == 1;  
           }
       }.run();
    }
   
    @Test
    public void conversationList() 
        throws Exception 
    {        
        new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 0;  
            }
        }.run();
        
        new FacesRequest("/pageWithoutDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 0;  
            }
        }.run();
        
        String conv1 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 1;  
            }
        }.run();
        
        String conv2 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 2;  
            }
        }.run();
        
        String conv3 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
               Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 3;  
            }
        }.run();
        
        
        new FacesRequest("/pageWithDescription", conv2) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 2;  
            }
        }.run();
        
        new FacesRequest("/pageWithDescription", conv1) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 1;  
            }
        }.run();
        
        
        new FacesRequest("/pageWithDescription", conv3) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                List<ConversationEntry> entries = (List<ConversationEntry>) getValue("#{conversationList}");               
                assert entries.size() == 0;  
            }
        }.run();
    }

    
    @Test
    public void switcher() 
        throws Exception 
    {
        new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 0;
                assert switcher.getConversationIdOrOutcome() == null;
            }
        }.run();
        
        
        final String conv1 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 1;
            }
        }.run();
        
        final String conv2 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 2;
            }
        }.run();
        
        final String conv3 = new FacesRequest("/pageWithDescription.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().beginConversation();
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 3;
            }
        }.run();
    
        new FacesRequest() {
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 3;
                
                List<SelectItem> items = switcher.getSelectItems();
                List<String> values = new ArrayList<String>();
                for (SelectItem item: items) {
                    assert item.getLabel().equals("page description");
                    values.add((String) item.getValue());
                }
                
                assert values.contains(conv1);
                assert values.contains(conv2);
                assert values.contains(conv3);
            }
        }.run();
        
        new FacesRequest("/pageWithDescription.xhtml", conv1) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
        }.run();
        
        new FacesRequest("/pageWithDescription.xhtml", conv2) {
            @Override
            protected void invokeApplication() throws Exception {
                Manager.instance().endConversation(true);
            }
            
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 1;
                assert switcher.getSelectItems().get(0).getLabel().equals("page description");
                assert switcher.getSelectItems().get(0).getValue().equals(conv3);
            }
        }.run();
        
        new FacesRequest("/pageWithAnotherDescription.xhtml", conv3) {
            @Override
            protected void renderResponse() throws Exception {
                Switcher switcher = (Switcher) getValue("#{switcher}");
                assert switcher.getSelectItems().size() == 1;
                
                assert switcher.getSelectItems().get(0).getLabel().equals("another page description");
                assert switcher.getSelectItems().get(0).getValue().equals(conv3);
            }
        }.run();
    }

   @Test
   public void killAllOthers() throws Exception
   {
      new FacesRequest("/pageWithAnotherDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            assert ConversationEntries.instance().size() == 1;
         }
      }.run();

      new FacesRequest("/pageWithoutDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            assert ConversationEntries.instance().size() == 2;
         }
      }.run();

      new FacesRequest("/pageWithDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
            Manager.instance().killAllOtherConversations();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            assert ConversationEntries.instance().size() == 1;
         }
      }.run();

   }

   @Test
   public void nestedKillAllOthers() throws Exception
   {

      final String unrelated = new FacesRequest("/pageWithoutDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            assert ConversationEntries.instance().size() == 1;
         }
      }.run();

      String root = new FacesRequest("/pageWithDescription.xhtml") {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            assert ConversationEntries.instance().size() == 2;
         }
      }.run();

      // nested conversation
      new FacesRequest("/pageWithDescription.xhtml", root) {
         @Override
         protected void invokeApplication() throws Exception
         {
            Manager.instance().beginNestedConversation();
         }

         @Override
         protected void renderResponse() throws Exception
         {
            assert ConversationEntries.instance().size() == 3;

            Manager.instance().killAllOtherConversations();
            assert ConversationEntries.instance().size() == 2;
            assert ConversationEntries.instance().getConversationIds()
                  .contains(unrelated) == false;
         }

      }.run();
   }
}
