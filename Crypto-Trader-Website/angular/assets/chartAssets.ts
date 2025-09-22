import {ChartDisplayProperties, SparkPoint} from "../models/chart/types";

export const defaultChartProperties: ChartDisplayProperties = {
    data: [{ date: new Date(), value: 0}],
    width: 500,
    height: 175,
    margin: {
        top: 20,
        right: 20,
        bottom: 30,
        left: 40
    },
    stroke: "#ffffff",
    strokeWidth: 2.0,
    // textColor: "#9aa0a6"
    textColor: "#979797"
}