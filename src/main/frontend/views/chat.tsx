import {TextField} from "@vaadin/react-components";
import {SetStateAction, useState} from "react";
import {ChatAiService} from "Frontend/generated/endpoints";
export default function Chat() {
    const [question, setQuestion] = useState<string | undefined>("")
    const [response, setResponse] = useState<string | undefined>("")

    async function send() {

        ChatAiService.ragChat(question).then((r: SetStateAction<string | undefined>)=>setResponse(r)).catch((err:any)=>{
            console.log(err)
        })
    }

    return(
        <div>
            <h1> chat page</h1>
            <div className="mt-2">
                <TextField style={{width:'50%'}} onChange={(e=>setQuestion(e.target.value))}></TextField>
                <button className="btn btn-success" onClick={send}>send</button>
            </div>
            <div className="mt-2">
                <div className="card">
                    <div className="card-body">
                        {response}
                    </div>
                </div>
            </div>
        </div>
    )
}