import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
async function ragChat_1(question: string | undefined, init?: EndpointRequestInit_1): Promise<string | undefined> { return client_1.call("ChatAiService", "ragChat", { question }, init); }
export { ragChat_1 as ragChat };
